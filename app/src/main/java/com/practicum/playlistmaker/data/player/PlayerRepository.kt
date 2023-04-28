package com.practicum.playlistmaker.data.player

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.domain.player.PlayerRepositoryInterface

class PlayerRepository(
    private val networkClient: NetworkClient
) : PlayerRepositoryInterface {
    //private val cachedTracks = mutableMapOf<String, Track>()
    private val mediaPlayer = MediaPlayer()

    enum class PlayerState { STATE_DEFAULT, STATE_PREPARED, STATE_PLAYING, STATE_PAUSED }

    override  var playerState = PlayerState.STATE_DEFAULT

    override fun preparePlayer(previewUrl: String) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun likeTrack(trackId: String) {
        /* В разработке. Вариант с управлением через сеть.
        val response = networkClient.doRequest(RequestGetTrack(trackId = trackId)) as TracksResponse

        if (response.resultCount == 1) {
            cachedTracks[trackId]?.let { track: Track ->
                cachedTracks[trackId] = track.copy(isLiked = false)
            }
        }
        */
    }

    override fun unlikeTrack(trackId: String) {
        /* В разработке. Вариант с управлением через сеть.
        val response = networkClient.doRequest(RequestGetTrack(trackId = trackId)) as TracksResponse

        if (response.resultCount == 1) {
            cachedTracks[trackId]?.let { track: Track ->
                cachedTracks[trackId] = track.copy(isLiked = true)
            }
        }
        */
    }

    override fun addTrackToPlaylist(trackId: String) {
        /* В разработке. Вариант с управлением через сеть.
        val response = networkClient.doRequest(RequestGetTrack(trackId = trackId)) as TracksResponse

        if (response.resultCount == 1) {
            cachedTracks[trackId]?.let { track: Track ->
                cachedTracks[trackId] = track.copy(isFavourite = false)
            }
        }
       */
    }

    override fun removeTrackFromPlaylist(trackId: String) {
        /* В разработке. Вариант с управлением через сеть.
        val response = networkClient.doRequest(RequestGetTrack(trackId = trackId)) as TracksResponse

        if (response.resultCount == 1) {
        cachedTracks[trackId]?.let { track: Track ->
            cachedTracks[trackId] = track.copy(isFavourite = true)
        }
        }
        */
    }

    // В разработке. Вариант с подгрузкой трека на этом шаге, а не через интент
    override fun getTrackForId(trackId: String) {
        /* В разработке
        val response = networkClient.doRequest(RequestGetTrack(trackId = trackId)) as Track

        cachedTracks[trackId] = response

        return response
        }
        */
    }
}