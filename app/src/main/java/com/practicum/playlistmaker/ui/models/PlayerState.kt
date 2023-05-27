package com.practicum.playlistmaker.ui.models

sealed interface PlayerState {

    object Loading: PlayerState

    data class Content(
        val trackUi: TrackUi,
    ): PlayerState
}
