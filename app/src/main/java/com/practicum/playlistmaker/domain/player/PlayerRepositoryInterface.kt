package com.practicum.playlistmaker.domain.player

import com.practicum.playlistmaker.data.player.PlayerRepository

interface PlayerRepositoryInterface {
    var playerState: PlayerRepository.PlayerState

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