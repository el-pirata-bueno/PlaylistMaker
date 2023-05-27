package com.practicum.playlistmaker.domain.player.impl

import com.practicum.playlistmaker.data.player.MediaPlayerState
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.TrackPlayer

class TrackPlayerInteractor(
    private val repository: TrackPlayer,
) : PlayerInteractor {
    //private val executor = Executors.newCachedThreadPool()

    override fun preparePlayer(trackUrl: String) {
        repository.preparePlayer(trackUrl)
    }

    override fun getPlayerState(): MediaPlayerState {
        return repository.playerState
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }

    override fun getTrackDuration(): Int {
        return repository.getTrackDuration()
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun releasePlayer() {
        repository.releasePlayer()
    }

    override fun likeTrack(
        trackId: Int
    ) {
        repository.likeTrack(trackId)
    }

    override fun unlikeTrack(
        trackId: Int
    ) {
        repository.unlikeTrack(trackId)
    }

    override fun addTrackToPlaylist(
        trackId: Int
    ) {
        repository.addTrackToPlaylist(trackId)
    }

    override fun removeTrackFromPlaylist(
        trackId: Int
    ) {
        repository.removeTrackFromPlaylist(trackId)
    }

    override fun getTrackForId(
        trackId: Int
    ) {
        /* В разработке - получение конкретного трека по api через его id
        executor.execute {
        consumer.consume(repository.getTrackForId(trackId))
        }
        }
        */
    }
}