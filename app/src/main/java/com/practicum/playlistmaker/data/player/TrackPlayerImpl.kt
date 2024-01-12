package com.practicum.playlistmaker.data.player

import android.media.MediaPlayer
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.data.db.LikedTracksDatabase
import com.practicum.playlistmaker.data.dto.TrackGetRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.data.model.TrackDto
import com.practicum.playlistmaker.data.model.TrackEntity
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.storage.impl.PlaylistsLocalStorage
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.player.TrackPlayer
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

const val ERROR = R.string.something_went_wrong.toString()
const val SERVER_ERROR = R.string.server_error.toString()

class TrackPlayerImpl (
    private val networkClient: NetworkClient,
    private val appDatabase: LikedTracksDatabase,
    private val trackDbConvertor: TrackDbConvertor,
    private val playlistsLocalStorage: PlaylistsLocalStorage,
    private val mediaPlayer: MediaPlayer
) : TrackPlayer {

    override var playerState = MediaPlayerState.STATE_DEFAULT

    val tracksInPlaylists = playlistsLocalStorage.getPlaylists()

    override fun preparePlayer(previewUrl: String) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = MediaPlayerState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = MediaPlayerState.STATE_PREPARED
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = MediaPlayerState.STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = MediaPlayerState.STATE_PAUSED
    }

    override fun releasePlayer() {
        mediaPlayer.reset()
    }

    override fun likeTrack(track: Track) {
        appDatabase.trackDao().insertLikedTrack(mapTrackToEntity(track))
    }

    override fun unlikeTrack(track: Track) {
        appDatabase.trackDao().deleteLikedTrack(mapTrackToEntity(track))
    }

    override fun addTrackToPlaylist(trackId: Long) {
        playlistsLocalStorage.addTrackToPlaylist(trackId)
    }

    override fun removeTrackFromPlaylist(trackId: Long) {
        playlistsLocalStorage.removeTrackFromPlaylist(trackId)
    }

    override fun getCurrentPosition(): Int = mediaPlayer.currentPosition
    override fun getTrackDuration(): Int = mediaPlayer.duration

    override fun getTrackFromId(trackId: Long): Flow<Resource<List<Track>>> = flow {
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

    private fun mapTrackToEntity(
        track: Track
    ): TrackEntity {
        return TrackEntity(
            id = 0,
            trackName = track.trackName ?: "",
            artistName = track.artistName ?: "",
            trackId = track.trackId ?: 0,
            trackTime = if (track.trackTime != null)
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime) else "",
            artworkUrl100 = track.artworkUrl100 ?: "",
            collectionName = track.collectionName ?: "",
            releaseDate = track.releaseDate ?: "",
            primaryGenreName = track.primaryGenreName ?: "",
            country = track.country ?: "",
            previewUrl = track.previewUrl ?: "",
            isFavorite = track.isFavorite,
            isInPlaylist = track.isInPlaylist
        )
    }
}