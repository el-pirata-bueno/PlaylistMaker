package com.practicum.playlistmaker.data.player

import android.media.MediaPlayer
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.dto.TrackGetRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.data.models.TrackDto
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.storage.LikesLocalStorage
import com.practicum.playlistmaker.data.storage.PlaylistsLocalStorage
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.player.TrackPlayer
import com.practicum.playlistmaker.util.Resource
import java.text.SimpleDateFormat
import java.util.Locale

class AudioTrackPlayer(
    private val networkClient: NetworkClient,
    private val likeLocalStorage: LikesLocalStorage,
    private val playlistsLocalStorage: PlaylistsLocalStorage,
) : TrackPlayer {

    private val mediaPlayer = MediaPlayer()
    override var playerState = MediaPlayerState.STATE_DEFAULT

    val tracksLiked = likeLocalStorage.getLiked()
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
        mediaPlayer.release()
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

    override fun getTrackFromId(trackId: Int): Resource<List<Track>> {
        val trackGetResponse = networkClient.doRequest(TrackGetRequest(trackId))

        return when (trackGetResponse.resultCode) {
            -1 -> {
                Resource.Error(R.string.something_went_wrong.toString())
            }

            200 -> {
                Resource.Success((trackGetResponse as TracksSearchResponse).results.map {
                    mapTrack(tracksLiked, tracksInPlaylists, it)
                })
            }

            else -> {
                Resource.Error(R.string.server_error.toString())
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