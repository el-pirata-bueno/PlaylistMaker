package com.practicum.playlistmaker.presentation.media

import com.practicum.playlistmaker.domain.model.Track

sealed interface MediaLikedState {
    object Empty: MediaLikedState
    data class Content(val tracks: List<Track>) : MediaLikedState
}