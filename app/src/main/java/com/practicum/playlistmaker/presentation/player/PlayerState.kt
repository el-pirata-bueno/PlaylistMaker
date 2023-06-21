package com.practicum.playlistmaker.presentation.player

import com.practicum.playlistmaker.ui.models.TrackUi

sealed interface PlayerState {

    //TODO Попробовать переделать на класс с полями и оперировать ими, а не в конструкторе дата-классов

    object Loading : PlayerState
    data class Error(val errorMessage: String) : PlayerState
    data class Content(val track: TrackUi, val isPlaying: Boolean, val currentTrackTime: String) :
        PlayerState

}
