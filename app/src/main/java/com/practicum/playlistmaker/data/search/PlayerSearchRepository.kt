package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.dto.TrackGetRequest
import com.practicum.playlistmaker.data.dto.TrackGetResponse
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.data.models.TrackDto
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.player.LikesLocalStorage
import com.practicum.playlistmaker.data.player.PlaylistsLocalStorage
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.util.Resource
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerSearchRepository(
    private val networkClient: NetworkClient,
    private val likeLocalStorage: LikesLocalStorage,
    private val playlistsLocalStorage: PlaylistsLocalStorage) : SearchRepository {

    val tracksLiked = likeLocalStorage.getLiked()
    val tracksInPlaylists = playlistsLocalStorage.getPlaylists()

    override fun searchTracks(term: String): Resource<List<Track>> {
        val tracksSearchResponse = networkClient.doRequest(TracksSearchRequest(term))
        return when (tracksSearchResponse.resultCode) {
            -1 -> {
                Resource.Error(R.string.something_went_wrong.toString())
            }

            200 -> {
                Resource.Success((tracksSearchResponse as TracksSearchResponse).results.map {
                    mapTrack(tracksLiked, tracksInPlaylists, it)
                })
            }

            else -> {
                Resource.Error(R.string.server_error.toString())
            }
        }
    }

    override fun getTrack(trackId: Int): Resource<List<Track>> {
        val trackGetResponse = networkClient.doRequest(TrackGetRequest(trackId))

        return when (trackGetResponse.resultCode) {
            -1 -> {
                Resource.Error(R.string.something_went_wrong.toString())
            }

            200 -> {
                Resource.Success((trackGetResponse as TrackGetResponse).results.map {
                    mapTrack(tracksLiked, tracksInPlaylists, it)
                })
            }

            else -> {
                Resource.Error(R.string.server_error.toString())
            }
        }
    }

    private fun mapTrack(tracksLiked: Set<String>, tracksInPlaylists: Set<String>, trackDto: TrackDto): Track {
        return Track(
            trackName = trackDto.trackName,
            artistName = trackDto.artistName,
            trackId = trackDto.trackId,
            trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackDto.trackTimeMillis),
            artworkUrl100 = trackDto.artworkUrl100,
            collectionName = trackDto.collectionName,
            releaseDate = trackDto.releaseDate,
            primaryGenreName = trackDto.primaryGenreName,
            country = trackDto.country,
            previewUrl = trackDto.previewUrl,
            isLiked = tracksLiked.contains(trackDto.trackId.toString()),
            isInPlaylist = tracksInPlaylists.contains(trackDto.trackId.toString())
        )
    }
}