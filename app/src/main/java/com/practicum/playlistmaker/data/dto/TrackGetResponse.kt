package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.data.models.TrackDto

data class TrackGetResponse(
    var resultCount: Int,
    val results: List<TrackDto>
): Response()