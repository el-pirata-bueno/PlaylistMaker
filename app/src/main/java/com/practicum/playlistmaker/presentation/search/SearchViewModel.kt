package com.practicum.playlistmaker.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.search.SearchInteractor
import com.practicum.playlistmaker.util.ErrorType
import com.practicum.playlistmaker.util.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private var historyTracks = ArrayList<Track>()
    private val searchStateLiveData = MutableLiveData<SearchState>()
    private var latestSearchText: String? = null
    fun getSearchStateLiveData(): LiveData<SearchState> = searchStateLiveData

    private val trackSearchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
        loadTracks(changedText)
    }

    fun fillData()  {
        var history: List<Track>
        viewModelScope.launch(Dispatchers.IO) {
            history = searchInteractor.getHistoryTracks()
            historyTracks.clear()
            historyTracks.addAll(history)
        }
        clearSearchText()
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

    fun onResume() {
        var history: List<Track>
        viewModelScope.launch {
            history = searchInteractor.getHistoryTracks()
            historyTracks.clear()
            historyTracks.addAll(history)
        }

        searchStateLiveData.postValue(
            SearchState.History(
                historyTracks = historyTracks,
                clearSearch = true
            )
        )
    }

    fun loadTracks(query: String) {
        if (query.isEmpty()) {
            searchStateLiveData.postValue(SearchState.History(historyTracks))
            return
        }
        searchStateLiveData.postValue(SearchState.Loading)

        viewModelScope.launch(Dispatchers.IO) {
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

    fun addTrackToHistory(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            searchInteractor.addTrackToHistory(track)
        }
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

    private fun loadTracksResult(foundTracks: List<Track>?, errorType: ErrorType?) {
        val tracks = ArrayList<Track>()
        if (foundTracks != null) {
            val sortedTracks = foundTracks.sortedWith(compareBy { !it.isFavorite })
            tracks.addAll(sortedTracks)
        }

        when {
            errorType != null -> {
                renderState(
                    SearchState.Error(errorType)
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

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}

