package com.practicum.playlistmaker.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.dto.TrackGetRequest
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.util.Creator.provideApi

class RetrofitNetworkClient(private val context: Context) : NetworkClient {

    private val api = provideApi()

    override fun doRequest(dto: Any): Response {
        if (isConnected() == false) {
            return Response().apply { resultCode = -1 }
        }
        when (dto) {
            is TracksSearchRequest -> {
                val tracksSearchResponse = api.searchTracks(dto.term).execute()
                val body = tracksSearchResponse.body() ?: Response()
                return body.apply { resultCode = tracksSearchResponse.code() }
            }

            is TrackGetRequest -> {
                val trackGetResponse = api.searchTrackById(dto.trackId).execute()
                val body = trackGetResponse.body() ?: Response()
                return body.apply { resultCode = trackGetResponse.code() }
            }

            else -> return Response().apply { resultCode = 400 }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

}