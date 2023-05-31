package com.practicum.playlistmaker.domain.player

import com.practicum.playlistmaker.data.player.MediaPlayerState
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.util.Resource

interface TrackPlayer {
    var playerState: MediaPlayerState

    fun preparePlayer(trackUrl: String)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()

    fun likeTrack(trackId: Int)
    fun unlikeTrack(trackId: Int)
    fun addTrackToPlaylist(trackId: Int)
    fun removeTrackFromPlaylist(trackId: Int)

    fun getCurrentPosition(): Int
    fun getTrackDuration(): Int

    fun getTrackFromId(trackId: Int): Resource<List<Track>>
}