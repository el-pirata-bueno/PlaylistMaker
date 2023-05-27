package com.practicum.playlistmaker.ui.search.view_model

import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.SearchInteractor
import com.practicum.playlistmaker.ui.models.HandlerRouter
import com.practicum.playlistmaker.ui.models.SearchState
import com.practicum.playlistmaker.ui.models.TrackUi
import com.practicum.playlistmaker.util.Creator

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val handlerRouter: HandlerRouter,
    private val errorText: String,
    private val messageText: String
) : ViewModel() {

    private lateinit var historyTracks: List<Track>

    private val searchStateLiveData = MutableLiveData<SearchState>()
    fun getSearchStateLiveData(): LiveData<SearchState> = searchStateLiveData

    override fun onCleared() {
        super.onCleared()
        handlerRouter.stopRunnable(null)
    }

    private val clearSearchTextButtonLiveData = MutableLiveData<Boolean>()
    fun getClearSearchTextButtonLiveData(): LiveData<Boolean> = clearSearchTextButtonLiveData

    private val clearSearchLiveData = MutableLiveData<Boolean>()
    fun getClearSearchLiveData(): LiveData<Boolean> = clearSearchLiveData

    fun clearSearchText() {
        val historyTracks = searchInteractor.getHistoryTracks()
        clearSearchTextButtonLiveData.postValue(false)
        searchStateLiveData.postValue(SearchState.History(historyTracks.map { mapTrackToUi(it) }, clearSearchState = true))
    }

    private var latestSearchText: String? = null

    fun loadTracks(query: String) {
        if (query.isEmpty()) {
            searchStateLiveData.postValue(SearchState.History(historyTracks.map { mapTrackToUi(it) }))
            return
        }
        searchStateLiveData.postValue(SearchState.Loading)

        searchInteractor.getTracks(query, object : SearchInteractor.GetTracksConsumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                val tracks = mutableListOf<TrackUi>()
                if (foundTracks != null) {
                    val sortedTracks = foundTracks.map { mapTrackToUi(it) }
                        .sortedWith(compareBy { !it.isLiked })
                    tracks.addAll(sortedTracks)
                }

                when {
                    errorMessage != null -> {
                        renderState(
                            SearchState.Error(
                                errorMessage = errorText
                            )
                        )
                        //showToast.postValue(errorMessage!!)
                    }

                    tracks.isEmpty() -> {
                        renderState(
                            SearchState.Empty(
                                message = messageText
                            )
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
        if (latestSearchText == changedText) {
            return
        }

        latestSearchText = changedText
        handlerRouter.stopRunnable(null)

        val searchRunnable = Runnable { loadTracks(changedText) }

        val postTime = SystemClock.uptimeMillis() + handlerRouter.SEARCH_DEBOUNCE_DELAY
        handlerRouter.postAtTime(searchRunnable, SEARCH_REQUEST_TOKEN, postTime)
    }

    private fun renderState(state: SearchState) {
        searchStateLiveData.postValue(state)
    }

    fun addTrackToHistory(track: TrackUi) {
        searchInteractor.addTrackToHistory(track.trackId)
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

    fun clearHistory() {
        //val historyTracks = searchInteractor.getHistoryTracks()
        searchInteractor.clearHistory()
        searchStateLiveData.postValue(SearchState.PreLoading(false))
        //searchStateLiveData.postValue(SearchState.History(historyTracks.map { mapTrackToUi(it) }))
    }

    fun searchFocusChanged(hasFocus: Boolean, text: String) {
        val historyTracks = searchInteractor.getHistoryTracks()
        if (hasFocus && text.isEmpty()) {
            searchStateLiveData.postValue(SearchState.History(historyTracks.map { mapTrackToUi(it) }))
        }
    }

    fun changeText(text: String) {
        searchStateLiveData.postValue(SearchState.PreLoading(text.isNotEmpty()))
        clearSearchTextButtonLiveData.postValue(true)
    }

    companion object {
        val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(errorText: String, messageText: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as App
                SearchViewModel(
                    searchInteractor = Creator.provideSearchInteractor(context = application),
                    handlerRouter = HandlerRouter(Looper.getMainLooper()),
                    errorText,
                    messageText
                )
            }
        }
    }

    /* Тост на будущее
    private val showToast = SingleLiveEvent<String>()
    fun observeToastState(): LiveData<String> = showToast

     */
}

