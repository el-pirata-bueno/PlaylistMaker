package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.data.db.LikedTracksDatabase
import com.practicum.playlistmaker.data.dto.TrackGetRequest
import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.data.model.TrackDto
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.storage.impl.PlaylistsLocalStorage
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

const val ERROR = R.string.something_went_wrong.toString()
const val SERVER_ERROR = R.string.server_error.toString()
class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: LikedTracksDatabase,
    private val trackDbConvertor: TrackDbConvertor,
    playlistsLocalStorage: PlaylistsLocalStorage
) : SearchRepository {

    private val tracksInPlaylists = playlistsLocalStorage.getPlaylists()

    override fun searchTracks(term: String): Flow<Resource<List<Track>>> = flow {
        val tracksSearchResponse = networkClient.doRequest(TracksSearchRequest(term))
        when (tracksSearchResponse.resultCode) {
            -1 -> {
                emit(Resource.Error(ERROR))
            }

            200 -> {
                with(tracksSearchResponse as TracksSearchResponse) {
                    val data = results.map {
                        val trackTime = if (it.trackTimeMillis != null) SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis) else ""
                        Track(it.trackId, it.trackName, it.artistName, it.collectionName, it.releaseDate, trackTime, it.artworkUrl100, it.primaryGenreName, it.country, it.previewUrl)
                    }
                    // "Перепроверить работу при возврате назад к списку найденных треков")
                    val likedTracks = appDatabase.trackDao().getTrackIds()
                    for (track in data) {
                        for (likedTrack in likedTracks) {
                            if (track.trackId == likedTrack) {
                                track.isFavorite = true
                                break
                            }
                        }
                    }
                    emit(Resource.Success(data))
                }
                /*
                emit(Resource.Success((tracksSearchResponse as TracksSearchResponse).results.map {
                    mapTrack(tracksLiked, tracksInPlaylists, it)
                }))
                */
            }

            else -> {
                emit(Resource.Error(SERVER_ERROR))
            }
        }
    }

    //возможно, удалить это полностью
    override fun getTrack(trackId: Long): Flow<Resource<List<Track>>> = flow {
        val trackGetResponse = networkClient.doRequest(TrackGetRequest(trackId))

        when (trackGetResponse.resultCode) {
            -1 -> {
                emit(Resource.Error(ERROR))
            }

            200 -> {
                val result = (trackGetResponse as TracksSearchResponse).results
                val isFavorite = appDatabase.trackDao().getTrackById(result[0].trackId) != null

                emit(Resource.Success(result.map {
                    mapTrack(isFavorite, tracksInPlaylists, it)
                }))
            }

            else -> {
                emit(Resource.Error(SERVER_ERROR))
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