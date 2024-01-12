package com.practicum.playlistmaker.domain.player

import com.practicum.playlistmaker.data.player.MediaPlayerState
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {
    fun preparePlayer(trackUrl: String)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()

    fun likeTrack(track: Track)
    fun unlikeTrack(track: Track)
    fun addTrackToPlaylist(trackId: Long)
    fun removeTrackFromPlaylist(trackId: Long)

    fun getPlayerState(): MediaPlayerState
    fun getCurrentPosition(): Int
    fun getTrackDuration(): Int

    fun getTrackFromId(trackId: Long): Flow<Pair<List<Track>?, String?>>

}
