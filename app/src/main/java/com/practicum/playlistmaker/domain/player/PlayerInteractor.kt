package com.practicum.playlistmaker.domain.player

import com.practicum.playlistmaker.data.player.MediaPlayerState
import com.practicum.playlistmaker.domain.model.Track

interface PlayerInteractor {
    fun preparePlayer(trackUrl: String)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()

    suspend fun likeTrack(track: Track)
    suspend fun unlikeTrack(track: Track)
    fun addTrackToPlaylist(track: Track)
    fun removeTrackFromPlaylist(track: Track)

    fun getPlayerState(): MediaPlayerState
    fun getCurrentPosition(): Int
    fun getTrackDuration(): Int

}
