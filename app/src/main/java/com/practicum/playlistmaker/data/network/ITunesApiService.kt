package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {
    @GET("/search")
    suspend fun searchTracks(@Query("term") term: String): TracksSearchResponse

    @GET("/lookup")
    suspend fun searchTrackById(@Query("id") term: Long): TracksSearchResponse
}
