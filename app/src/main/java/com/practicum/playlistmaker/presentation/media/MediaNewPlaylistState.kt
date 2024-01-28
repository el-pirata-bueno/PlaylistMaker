package com.practicum.playlistmaker.presentation.media

sealed interface MediaNewPlaylistState {
    object Content : MediaNewPlaylistState
}