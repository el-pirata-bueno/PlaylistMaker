package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.data.player.PlayerState
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.player.PlayerRepository
import java.util.concurrent.Executors

class TrackPlayerInteractor(
    private val repository: PlayerRepository,
) : PlayerInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun preparePlayer(trackUrl: String) {
        repository.preparePlayer(trackUrl)
    }

    override fun getPlayerState(): PlayerState {
        return repository.playerState
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
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
        trackId: String,
        consumer: com.practicum.playlistmaker.domain.api.PlayerInteractor.PlayerInfoConsumer
    ) {
        /* В разработке
        executor.execute {
            repository.likeTrack(trackId)
            consumer.consume(repository.getTrackForId(trackId))
        }
        */
    }

    override fun unlikeTrack(
        trackId: String,
        consumer: com.practicum.playlistmaker.domain.api.PlayerInteractor.PlayerInfoConsumer
    ) {
        /* В разработке
        executor.execute {
            repository.unlikeTrack(trackId)
            consumer.consume(repository.getTrackForId(trackId))
        }
        */
    }

    override fun addTrackToPlaylist(
        trackId: String,
        consumer: com.practicum.playlistmaker.domain.api.PlayerInteractor.PlayerInfoConsumer
    ) {
        /* В разработке
        executor.execute {
        repository.addTrackToPlaylist(trackId)
        consumer.consume(repository.getTrackForId(trackId))
        }

        */
    }

    override fun removeTrackFromPlaylist(
        trackId: String,
        consumer: com.practicum.playlistmaker.domain.api.PlayerInteractor.PlayerInfoConsumer
    ) {
        /* В разработке
        executor.execute {
        repository.removeTrackFromPlaylist(trackId)
        consumer.consume(repository.getTrackForId(trackId))
        }
        */
    }

    override fun getTrackForId(
        trackId: String,
        consumer: com.practicum.playlistmaker.domain.api.PlayerInteractor.PlayerInfoConsumer
    ) {
        /* В разработке
        executor.execute {
        consumer.consume(repository.getTrackForId(trackId))
        }
        }
        */
    }
}