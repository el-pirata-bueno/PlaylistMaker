package com.practicum.playlistmaker.domain.player

import com.practicum.playlistmaker.data.player.MediaPlayerState
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

interface TrackPlayer {
    var playerState: MediaPlayerState

    fun preparePlayer(trackUrl: String)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()

    fun likeTrack(track: Track)
    fun unlikeTrack(track: Track)
    fun addTrackToPlaylist(trackId: Long)
    fun removeTrackFromPlaylist(trackId: Long)

    fun getCurrentPosition(): Int
    fun getTrackDuration(): Int

    fun getTrackFromId(trackId: Long): Flow<Resource<List<Track>>>
}