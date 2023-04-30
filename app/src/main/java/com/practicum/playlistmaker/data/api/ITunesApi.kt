package com.practicum.playlistmaker.data.api

import com.practicum.playlistmaker.data.dto.TracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search")
    fun searchTracks(@Query("term") term: String): Call<TracksResponse>

    @GET("/lookup")
    fun searchTrackById(@Query("id") term: String): Call<TracksResponse>
}