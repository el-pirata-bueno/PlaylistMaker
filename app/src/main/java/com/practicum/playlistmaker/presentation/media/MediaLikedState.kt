package com.practicum.playlistmaker.presentation.media

import com.practicum.playlistmaker.domain.model.Track

sealed interface MediaLikedState {
    data class Empty(val message: String) : MediaLikedState
    data class Content(val tracks: List<Track>) : MediaLikedState
}