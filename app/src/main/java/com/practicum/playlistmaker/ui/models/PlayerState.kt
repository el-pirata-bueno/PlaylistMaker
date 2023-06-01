package com.practicum.playlistmaker.ui.models

sealed interface PlayerState {

    object Loading : PlayerState
    data class Error(val errorMessage: String) : PlayerState
    data class Content(val track: TrackUi, val isPlaying: Boolean, val currentTrackTime: String) : PlayerState

}

