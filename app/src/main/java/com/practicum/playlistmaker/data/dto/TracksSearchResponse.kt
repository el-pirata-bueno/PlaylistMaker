package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.data.model.TrackDto

data class TracksSearchResponse(
    var resultCount: Int,
    val results: List<TrackDto>
) : Response()