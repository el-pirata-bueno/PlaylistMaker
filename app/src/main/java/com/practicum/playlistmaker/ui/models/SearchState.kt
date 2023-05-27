package com.practicum.playlistmaker.ui.models

sealed interface SearchState {

    object Loading : SearchState

    data class PreLoading(
        val buttonVisible: Boolean
    ) : SearchState

    data class Content(
        val tracks: List<TrackUi>
    ) : SearchState

    data class History(
        val historyTracks: List<TrackUi>,
        val clearSearchState: Boolean = false
    ) : SearchState

    data class Error(
        val errorMessage: String
    ) : SearchState

    data class Empty(
        val message: String
    ) : SearchState

}
