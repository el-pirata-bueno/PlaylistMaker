package com.practicum.playlistmaker.domain.player.impl

import com.practicum.playlistmaker.data.player.MediaPlayerState
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.TrackPlayer

class PlayerInteractorImpl(
    private val repository: TrackPlayer,
) : PlayerInteractor {

    override fun preparePlayer(trackUrl: String) = repository.preparePlayer(trackUrl)
    override fun startPlayer() = repository.startPlayer()
    override fun pausePlayer() = repository.pausePlayer()
    override fun releasePlayer() = repository.releasePlayer()

    override suspend fun likeTrack(track: Track) = repository.likeTrack(track)
    override suspend fun unlikeTrack(track: Track) = repository.unlikeTrack(track)

    override fun getPlayerState(): MediaPlayerState = repository.playerState
    override fun getCurrentPosition(): Int = repository.getCurrentPosition()
    override fun getTrackDuration(): Int = repository.getTrackDuration()

}