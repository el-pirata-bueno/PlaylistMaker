package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.data.models.TrackDto
import com.practicum.playlistmaker.data.dto.TracksResponse
import com.practicum.playlistmaker.domain.api.ITunesApi
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.SearchRepositoryInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRepository(private val api: ITunesApi) : SearchRepositoryInterface {

    override fun search(query: String, onSuccess: (List<Track>) -> Unit, onError: () -> Unit) {
        api.searchTracks(query)
            .enqueue(object : Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    if (response.code() == 200) {
                        onSuccess.invoke(response.body()?.results!!.map { mapTrack(it) })
                    } else {
                        onError.invoke()
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    onError.invoke()
                }
            }
            )
    }

    private fun mapTrack(trackDto: TrackDto): Track {
        return Track(
            trackDto.trackName,
            trackDto.artistName,
            trackDto.trackId,
            trackDto.trackTimeMillis,
            trackDto.artworkUrl100,
            trackDto.collectionName,
            trackDto.releaseDate,
            trackDto.primaryGenreName,
            trackDto.country,
            trackDto.previewUrl,
            trackDto.isLiked,
            trackDto.isFavourite
        )
    }
}