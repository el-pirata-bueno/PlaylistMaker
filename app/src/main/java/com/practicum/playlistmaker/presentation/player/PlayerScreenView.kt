package com.practicum.playlistmaker.presentation.player

import com.practicum.playlistmaker.presentation.models.TrackUi

interface PlayerScreenView {
    fun addedToFavourites(added: Boolean)
    fun addedToPlaylist(added: Boolean)
    fun startedTrack()
    fun pausedTrack()
    fun playerPrepared()
    fun drawTrack(track: TrackUi?)
    fun goBack()
    fun startTimer()
    fun updateTimer(): Runnable
    //fun playerCompleted()
}