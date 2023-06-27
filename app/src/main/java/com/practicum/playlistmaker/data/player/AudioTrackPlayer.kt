package com.practicum.playlistmaker.data.player

import android.media.MediaPlayer
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.dto.TrackGetRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.data.models.TrackDto
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.storage.impl.LikesLocalStorage
import com.practicum.playlistmaker.data.storage.impl.PlaylistsLocalStorage
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.player.TrackPlayer
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

class AudioTrackPlayer(
    private val networkClient: NetworkClient,
    private val likeLocalStorage: LikesLocalStorage,
    private val playlistsLocalStorage: PlaylistsLocalStorage,
    private val mediaPlayer: MediaPlayer
) : TrackPlayer {

    override var playerState = MediaPlayerState.STATE_DEFAULT

    private val tracksLiked = likeLocalStorage.getLiked()
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

    override fun likeTrack(trackId: Int) {
        likeLocalStorage.likeTrack(trackId)
    }

    override fun unlikeTrack(trackId: Int) {
        likeLocalStorage.unlikeTrack(trackId)
    }

    override fun addTrackToPlaylist(trackId: Int) {
        playlistsLocalStorage.addTrackToPlaylist(trackId)
    }

    override fun removeTrackFromPlaylist(trackId: Int) {
        playlistsLocalStorage.removeTrackFromPlaylist(trackId)
    }

    override fun getCurrentPosition(): Int = mediaPlayer.currentPosition
    override fun getTrackDuration(): Int = mediaPlayer.duration

    override fun getTrackFromId(trackId: Int): Flow<Resource<List<Track>>> = flow {
        val trackGetResponse = networkClient.doRequest(TrackGetRequest(trackId))

        when (trackGetResponse.resultCode) {
            -1 -> {
                emit(Resource.Error(R.string.something_went_wrong.toString()))
            }

            200 -> {
                emit(Resource.Success((trackGetResponse as TracksSearchResponse).results.map {
                    mapTrack(tracksLiked, tracksInPlaylists, it)
                }))
            }

            else -> {
                emit(Resource.Error(R.string.server_error.toString()))
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