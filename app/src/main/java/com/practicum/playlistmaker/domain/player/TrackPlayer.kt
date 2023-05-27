package com.practicum.playlistmaker.domain.player

import com.practicum.playlistmaker.data.player.MediaPlayerState

interface TrackPlayer {
    var playerState: MediaPlayerState

    fun preparePlayer(trackUrl: String)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()

    // Поиск по сикбару в плеере
    // fun seek(trackId: Int, position: Float)

    fun likeTrack(trackId: Int)
    fun unlikeTrack(trackId: Int)
    fun addTrackToPlaylist(trackId: Int)
    fun removeTrackFromPlaylist(trackId: Int)

    fun getTrackForId(trackId: Int)
    fun getCurrentPosition(): Int
    fun getTrackDuration(): Int

    // Для сикбара в плеере
    interface StatusObserver {
        fun onProgress(progress: Float)
        fun onStop()
        fun onPlay()
    }
}