package com.practicum.playlistmaker.domain.player

import com.practicum.playlistmaker.data.player.PlayerState

interface PlayerRepository {
    var playerState: PlayerState

    fun preparePlayer(trackUrl: String)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun likeTrack(trackId: String)
    fun unlikeTrack(trackId: String)
    fun addTrackToPlaylist(trackId: String)
    fun removeTrackFromPlaylist(trackId: String)
    fun getTrackForId(trackId: String) // В перспективе будет вовзращать трек
    fun getCurrentPosition(): Int
}