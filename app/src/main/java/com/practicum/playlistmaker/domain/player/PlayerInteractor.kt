package com.practicum.playlistmaker.domain.player

import com.practicum.playlistmaker.data.player.MediaPlayerState
import com.practicum.playlistmaker.domain.models.Track

interface PlayerInteractor {
    fun preparePlayer(trackUrl: String)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()

    fun likeTrack(trackId: Int)
    fun unlikeTrack(trackId: Int)
    fun addTrackToPlaylist(trackId: Int)
    fun removeTrackFromPlaylist(trackId: Int)

    fun getTrackForId(trackId: Int)

    interface PlayerInfoConsumer {
        fun consume(track: Track)
    }

    fun getPlayerState(): MediaPlayerState
    fun getCurrentPosition(): Int
    fun getTrackDuration(): Int
}
