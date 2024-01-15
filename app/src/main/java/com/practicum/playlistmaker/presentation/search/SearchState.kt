package com.practicum.playlistmaker.presentation.search

import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.util.ErrorType

sealed interface SearchState {

    object Loading : SearchState
    object Empty: SearchState
    data class Error(val errorType: ErrorType): SearchState
    data class PreLoading(val buttonVisible: Boolean): SearchState
    data class Content(val tracks: ArrayList<Track>): SearchState
    //clearSearch - факт очистки истории для того, чтобы убрать клавиатуру и очистить текст поиска
    data class History(val historyTracks: List<Track>, val clearSearch: Boolean = false) :
        SearchState

}
