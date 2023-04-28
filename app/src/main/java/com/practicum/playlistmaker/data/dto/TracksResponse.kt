package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.data.models.TrackDto

data class TracksResponse(
    val resultCount: Int,
    val results: List<TrackDto>
)