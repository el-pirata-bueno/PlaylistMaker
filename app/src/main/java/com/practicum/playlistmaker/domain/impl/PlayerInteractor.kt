package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.data.player.PlayerRepository
import com.practicum.playlistmaker.domain.api.PlayerInteractorInterface
import com.practicum.playlistmaker.domain.player.PlayerRepositoryInterface
import java.util.concurrent.Executors

class PlayerInteractor(
    private val repository: PlayerRepositoryInterface,
) : PlayerInteractorInterface {
    private val executor = Executors.newCachedThreadPool()

    override fun preparePlayer(trackUrl: String) {
        repository.preparePlayer(trackUrl)
    }

    override fun getPlayerState(): PlayerRepository.PlayerState {
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
        consumer: PlayerInteractorInterface.PlayerInfoConsumer
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
        consumer: PlayerInteractorInterface.PlayerInfoConsumer
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
        consumer: PlayerInteractorInterface.PlayerInfoConsumer
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
        consumer: PlayerInteractorInterface.PlayerInfoConsumer
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
        consumer: PlayerInteractorInterface.PlayerInfoConsumer
    ) {
        /* В разработке
        executor.execute {
        consumer.consume(repository.getTrackForId(trackId))
        }
        }
        */
    }
}