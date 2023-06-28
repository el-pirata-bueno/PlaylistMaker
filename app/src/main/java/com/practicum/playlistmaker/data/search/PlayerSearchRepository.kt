package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.dto.TrackGetRequest
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.data.models.TrackDto
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.storage.impl.LikesLocalStorage
import com.practicum.playlistmaker.data.storage.impl.PlaylistsLocalStorage
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

const val ERROR = R.string.something_went_wrong.toString()
const val SERVER_ERROR = R.string.server_error.toString()
class PlayerSearchRepository(
    private val networkClient: NetworkClient,
    likeLocalStorage: LikesLocalStorage,
    playlistsLocalStorage: PlaylistsLocalStorage
) : SearchRepository {

    private val tracksLiked = likeLocalStorage.getLiked()
    private val tracksInPlaylists = playlistsLocalStorage.getPlaylists()

    override fun searchTracks(term: String): Flow<Resource<List<Track>>> = flow {
        val tracksSearchResponse = networkClient.doRequest(TracksSearchRequest(term))
        when (tracksSearchResponse.resultCode) {
            -1 -> {
                emit(Resource.Error(ERROR))
            }

            200 -> {
                emit(Resource.Success((tracksSearchResponse as TracksSearchResponse).results.map {
                    mapTrack(tracksLiked, tracksInPlaylists, it)
                }))
            }

            else -> {
                emit(Resource.Error(SERVER_ERROR))
            }
        }
    }

    override fun getTrack(trackId: Int): Flow<Resource<List<Track>>> = flow {
        val trackGetResponse = networkClient.doRequest(TrackGetRequest(trackId))

        when (trackGetResponse.resultCode) {
            -1 -> {
                emit(Resource.Error(ERROR))
            }

            200 -> {
                emit(Resource.Success((trackGetResponse as TracksSearchResponse).results.map {
                    mapTrack(tracksLiked, tracksInPlaylists, it)
                }))
            }

            else -> {
                emit(Resource.Error(SERVER_ERROR))
            }
        }
    }

    private fun mapTrack(
        tracksLiked: Set<String>,
        tracksInPlaylists: Set<String>,
        trackDto: TrackDto
    ): Track {
        return Track(
            trackName = trackDto.trackName ?: "",
            artistName = trackDto.artistName ?: "",
            trackId = trackDto.trackId ?: 0,
            trackTime = if (trackDto.trackTimeMillis != null)
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackDto.trackTimeMillis) else "",
            artworkUrl100 = trackDto.artworkUrl100 ?: "",
            collectionName = trackDto.collectionName ?: "",
            releaseDate = trackDto.releaseDate ?: "",
            primaryGenreName = trackDto.primaryGenreName ?: "",
            country = trackDto.country ?: "",
            previewUrl = trackDto.previewUrl ?: "",
            isLiked = tracksLiked.contains(trackDto.trackId.toString()),
            isInPlaylist = tracksInPlaylists.contains(trackDto.trackId.toString())
        )
    }
}