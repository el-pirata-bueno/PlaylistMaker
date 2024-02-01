package com.practicum.playlistmaker.ui.media.playlists

import com.practicum.playlistmaker.domain.model.Playlist

sealed interface MediaNewEditPlaylistState {
    object NewPlaylist : MediaNewEditPlaylistState
    data class EditPlaylist(
        val playlist: Playlist?
    ) : MediaNewEditPlaylistState
}

