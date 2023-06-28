package com.practicum.playlistmaker.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.SearchInteractor
import com.practicum.playlistmaker.ui.models.TrackUi
import com.practicum.playlistmaker.util.debounce
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val historyTracks = ArrayList<TrackUi>()
    private val searchStateLiveData = MutableLiveData<SearchState>()

    private var latestSearchText: String? = null
    fun getSearchStateLiveData(): LiveData<SearchState> = searchStateLiveData

    private val trackSearchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
        loadTracks(changedText)
    }

    init {
        historyTracks.addAll(searchInteractor.getHistoryTracks().map { mapTrackToUi(it) })
    }

    fun clearSearchText() {
        if (historyTracks.isNotEmpty()) {
            searchStateLiveData.postValue(
                SearchState.History(
                    historyTracks = historyTracks,
                    clearSearch = true
                )
            )
        }
        else {
            searchStateLiveData.postValue(SearchState.PreLoading(false))
        }
    }

    fun loadTracks(query: String) {
        if (query.isEmpty()) {
            searchStateLiveData.postValue(SearchState.History(historyTracks))
            return
        }
        searchStateLiveData.postValue(SearchState.Loading)

        viewModelScope.launch {
            searchInteractor
                .getTracks(query)
                .collect { pair ->
                    loadTracksResult(pair.first, pair.second)
                }
        }
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            trackSearchDebounce(changedText)
        }
    }

    fun addTrackToHistory(track: TrackUi) {
        viewModelScope.launch {
            searchInteractor
                .getOneTrack(track.trackId)
                .collect { pair ->
                    getTrackResult(pair.first, pair.second)
                }
        }

        //TODO searchInteractor.addTrackToHistory(track.trackId)
    }

    fun clearHistory() {
        historyTracks.clear()
        searchInteractor.clearHistory()
        searchStateLiveData.postValue(SearchState.History(historyTracks))
    }

    fun searchFocusChanged(hasFocus: Boolean, text: String) {
        if (hasFocus && text.isEmpty()) {
            searchStateLiveData.postValue(SearchState.History(historyTracks))
        }
    }

    fun changeText(text: String) {
        searchStateLiveData.postValue(SearchState.PreLoading(text.isNotEmpty()))
    }

    private fun renderState(state: SearchState) {
        searchStateLiveData.postValue(state)
    }

    private fun loadTracksResult(foundTracks: List<Track>?, errorMessage: String?) {
        val tracks = ArrayList<TrackUi>()
        if (foundTracks != null) {
            val sortedTracks = foundTracks.map { mapTrackToUi(it) }
                .sortedWith(compareBy { !it.isLiked })
            tracks.addAll(sortedTracks)
        }

        when {
            errorMessage != null -> {
                renderState(
                    SearchState.Error(errorMessage = errorMessage)
                )
            }

            tracks.isEmpty() -> {
                renderState(
                    SearchState.Empty
                )
            }

            else -> {
                renderState(
                    SearchState.Content(
                        tracks = tracks,
                    )
                )
            }
        }
    }

    private fun getTrackResult(foundTracks: List<Track>?, errorMessage: String?) {
        var track: Track
        if (foundTracks != null) {
            track = foundTracks[0]
            searchInteractor.addTrackToHistory(track)
        }
    }


    private fun mapTrackToUi(track: Track): TrackUi {
        return TrackUi(
            track.trackName,
            track.artistName,
            track.trackId,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isLiked,
            track.isInPlaylist
        )
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}

