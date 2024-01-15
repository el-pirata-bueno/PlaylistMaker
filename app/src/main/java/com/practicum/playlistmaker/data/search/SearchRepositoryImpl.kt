package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.data.model.TrackDto
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.storage.impl.PlaylistsLocalStorage
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.util.ErrorType
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val likedTracksIdsRepositoryImpl: LikedTracksIdsRepositoryImpl,
    playlistsLocalStorage: PlaylistsLocalStorage
) : SearchRepository {


    private val tracksInPlaylists = playlistsLocalStorage.getPlaylists()

    override fun searchTracks(term: String): Flow<Resource<List<Track>>> = flow {
        val tracksSearchResponse = networkClient.doRequest(TracksSearchRequest(term))
        when (tracksSearchResponse.resultCode) {
            -1 -> {
                emit(Resource.Error(ErrorType.ConnectionError))
            }

            200 -> {
                with(tracksSearchResponse as TracksSearchResponse) {
                    val data = results.map {
                        mapTrack(false, tracksInPlaylists, it)
                    }
                    var likedTracksIds = likedTracksIdsRepositoryImpl.getLikedTracksIds()

                    for (track in data) {
                        for (likedTrack in likedTracksIds) {
                            if (track.trackId == likedTrack) {
                                track.isFavorite = true
                                break
                            }
                        }
                    }
                    emit(Resource.Success(data))
                }
            }

            else -> {
                emit(Resource.Error(ErrorType.ServerError))
            }
        }
    }

    private fun mapTrack(
        isFavorite: Boolean,
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
            isFavorite = isFavorite,
            isInPlaylist = tracksInPlaylists.contains(trackDto.trackId.toString())
        )
    }
}