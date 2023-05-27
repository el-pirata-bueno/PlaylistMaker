package com.practicum.playlistmaker.data.player

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.player.TrackPlayer

class AudioTrackPlayer(
    private val ITunesNetworkClient: RetrofitNetworkClient,
    private val likeLocalStorage: LikesLocalStorage,
    private val playlistsLocalStorage: PlaylistsLocalStorage,
) : TrackPlayer {
    private val mediaPlayer = MediaPlayer()

    override var playerState = MediaPlayerState.STATE_DEFAULT

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

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun getTrackDuration(): Int {
        return mediaPlayer.duration
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

    // В разработке. Вариант с подгрузкой трека на этом шаге, а не через интент
    override fun getTrackForId(trackId: Int) {

    }
}