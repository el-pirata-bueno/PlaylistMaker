package com.practicum.playlistmaker.domain.player.impl

import com.practicum.playlistmaker.data.player.MediaPlayerState
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.TrackPlayer
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class TrackPlayerInteractor(
    private val repository: TrackPlayer,
) : PlayerInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun preparePlayer(trackUrl: String) = repository.preparePlayer(trackUrl)
    override fun startPlayer() = repository.startPlayer()
    override fun pausePlayer() = repository.pausePlayer()
    override fun releasePlayer() = repository.releasePlayer()

    override fun likeTrack(trackId: Int) = repository.likeTrack(trackId)
    override fun unlikeTrack(trackId: Int) = repository.unlikeTrack(trackId)
    override fun addTrackToPlaylist(trackId: Int) = repository.addTrackToPlaylist(trackId)
    override fun removeTrackFromPlaylist(trackId: Int) = repository.removeTrackFromPlaylist(trackId)

    override fun getPlayerState(): MediaPlayerState = repository.playerState
    override fun getCurrentPosition(): Int = repository.getCurrentPosition()
    override fun getTrackDuration(): Int = repository.getTrackDuration()

    override fun getTrackFromId(trackId: Int): Flow<Pair<List<Track>?, String?>> {
        return repository.getTrackFromId(trackId).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}