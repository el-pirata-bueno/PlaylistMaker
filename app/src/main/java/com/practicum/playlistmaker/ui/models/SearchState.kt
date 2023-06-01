package com.practicum.playlistmaker.ui.models

sealed interface SearchState {

    object Loading : SearchState
    object Empty:  SearchState

    data class Error(val errorMessage: String): SearchState
    data class PreLoading(val buttonVisible: Boolean): SearchState
    data class Content(val tracks: ArrayList<TrackUi>): SearchState
    //clearSearch - факт очистки истории для того, чтобы убрать клавиатуру и очистить текст поиска
    data class History(val historyTracks: ArrayList<TrackUi>, val clearSearch: Boolean = false) : SearchState

}
