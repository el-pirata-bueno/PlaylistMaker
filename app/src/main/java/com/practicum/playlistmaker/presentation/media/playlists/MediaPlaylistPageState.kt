package com.practicum.playlistmaker.presentation.media.playlists

import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track

sealed interface MediaPlaylistPageState {

    object PreLoading : MediaPlaylistPageState
    data class Content(
        val playlist: Playlist?,
        val tracks: List<Track>,
        val playlistLength: Int
    ) : MediaPlaylistPageState

    object MenuBottomSheet : MediaPlaylistPageState
}

