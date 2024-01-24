package com.practicum.playlistmaker.presentation.media

import com.practicum.playlistmaker.domain.model.Track

sealed interface MediaLikedTracksState {
    object Empty: MediaLikedTracksState
    data class Content(val tracks: List<Track>) : MediaLikedTracksState
}