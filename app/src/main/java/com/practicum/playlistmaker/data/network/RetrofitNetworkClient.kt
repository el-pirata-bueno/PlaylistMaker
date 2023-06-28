package com.practicum.playlistmaker.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.dto.TrackGetRequest
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val context: Context, private val api: ITunesApiService) :
    NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        return withContext(Dispatchers.IO) {
            try {
                when (dto) {
                    is TracksSearchRequest -> {
                        val tracksSearchResponse = api.searchTracks(dto.term)
                        //val body = tracksSearchResponse.body() ?: Response()
                        //body.apply { resultCode = tracksSearchResponse.code() }
                        tracksSearchResponse.apply { resultCode = 200 }
                    }

                    is TrackGetRequest -> {
                        val trackGetResponse = api.searchTrackById(dto.trackId)
                        //val body = trackGetResponse.body() ?: Response()
                        //body.apply { resultCode = trackGetResponse.code() }
                        trackGetResponse.apply { resultCode = 200 }
                    }

                    else -> Response().apply { resultCode = 400 }
                }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
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