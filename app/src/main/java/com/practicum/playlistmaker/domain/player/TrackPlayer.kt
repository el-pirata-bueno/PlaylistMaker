package com.practicum.playlistmaker.domain.player

import com.practicum.playlistmaker.data.player.MediaPlayerState
import com.practicum.playlistmaker.domain.model.Track

interface TrackPlayer {
    var playerState: MediaPlayerState

    fun preparePlayer(trackUrl: String)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()

    suspend fun likeTrack(track: Track)
    suspend fun unlikeTrack(track: Track)

    fun getCurrentPosition(): Int
    fun getTrackDuration(): Int

}