package com.practicum.playlistmaker.data.player

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.converters.TrackMapper
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.search.SearchHistory
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.player.TrackPlayer

class TrackPlayerImpl (
    private val appDatabase: AppDatabase,
    private val searchHistory: SearchHistory,
    private val mediaPlayer: MediaPlayer,
    private val trackMapper: TrackMapper
    ) : TrackPlayer {

    override var playerState = MediaPlayerState.STATE_DEFAULT

    override fun preparePlayer(previewUrl: String) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = MediaPlayerState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = MediaPlayerState.STATE_PREPARED
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = MediaPlayerState.STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = MediaPlayerState.STATE_PAUSED
    }

    override fun releasePlayer() {
        mediaPlayer.reset()
    }

    override suspend fun likeTrack(track: Track) {
        appDatabase.trackDao().insertLikedTrack(trackMapper.mapTrackToEntity(track))
        searchHistory.addTrackToHistory(track)
    }

    override suspend fun unlikeTrack(track: Track) {
        appDatabase.trackDao().deleteLikedTrack(trackMapper.mapTrackToEntity(track))
        searchHistory.addTrackToHistory(track)
    }

    override fun getCurrentPosition(): Int = mediaPlayer.currentPosition
    override fun getTrackDuration(): Int = mediaPlayer.duration
}