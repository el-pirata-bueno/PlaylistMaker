package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.data.models.TrackDto

data class TracksSearchResponse(
    var resultCount: Int,
    val results: List<TrackDto>
) : Response()