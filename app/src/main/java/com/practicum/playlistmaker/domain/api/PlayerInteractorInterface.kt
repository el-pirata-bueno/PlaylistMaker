package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.data.player.PlayerRepository
import com.practicum.playlistmaker.domain.models.Track

interface PlayerInteractorInterface {
    fun preparePlayer(trackUrl: String)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun likeTrack(trackId: String, consumer: PlayerInfoConsumer)
    fun unlikeTrack(trackId: String, consumer: PlayerInfoConsumer)
    fun addTrackToPlaylist(trackId: String, consumer: PlayerInfoConsumer)
    fun removeTrackFromPlaylist(trackId: String, consumer: PlayerInfoConsumer)
    fun getTrackForId(trackId: String, consumer: PlayerInfoConsumer)

    interface PlayerInfoConsumer {
        fun consume(track: Track)
    }

    fun getPlayerState(): PlayerRepository.PlayerState
    fun getCurrentPosition(): Int
}
