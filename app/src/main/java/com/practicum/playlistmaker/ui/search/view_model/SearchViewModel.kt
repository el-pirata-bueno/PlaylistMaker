package com.practicum.playlistmaker.ui.search.view_model

import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.SearchInteractor
import com.practicum.playlistmaker.ui.models.HandlerRouter
import com.practicum.playlistmaker.ui.models.SearchState
import com.practicum.playlistmaker.ui.models.TrackUi

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val handlerRouter: HandlerRouter
) : ViewModel() {

    private val historyTracks = ArrayList<TrackUi>()
    private val searchStateLiveData = MutableLiveData<SearchState>()
    fun getSearchStateLiveData(): LiveData<SearchState> = searchStateLiveData

    init {
        historyTracks.addAll(searchInteractor.getHistoryTracks().map { mapTrackToUi(it) })
    }

    override fun onCleared() {
        super.onCleared()
        handlerRouter.stopRunnable(null)
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

        searchInteractor.getTracks(query, object : SearchInteractor.GetTracksConsumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
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
        }
        )
    }

    fun searchDebounce(changedText: String) {
        handlerRouter.stopRunnable(null)

        val searchRunnable = Runnable { loadTracks(changedText) }

        val postTime = SystemClock.uptimeMillis() + handlerRouter.SEARCH_DEBOUNCE_DELAY
        handlerRouter.postAtTime(searchRunnable, SEARCH_REQUEST_TOKEN, postTime)
    }

    fun addTrackToHistory(track: TrackUi) {
        searchInteractor.addTrackToHistory(track.trackId)
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
        val SEARCH_REQUEST_TOKEN = Any()
    }
}

