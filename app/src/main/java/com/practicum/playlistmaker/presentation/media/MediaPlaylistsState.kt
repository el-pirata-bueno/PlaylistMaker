package com.practicum.playlistmaker.presentation.media

import com.practicum.playlistmaker.domain.model.Playlist

sealed interface MediaPlaylistsState {
    object Empty : MediaPlaylistsState
    data class Content(val playlists: List<Playlist>) : MediaPlaylistsState

}