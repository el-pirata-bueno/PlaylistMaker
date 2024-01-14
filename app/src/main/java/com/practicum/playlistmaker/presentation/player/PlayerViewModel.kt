package com.practicum.playlistmaker.presentation.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.data.player.MediaPlayerState
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    val trackId: Long,
    val trackName: String?,
    val artistName: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val trackTime: String?,
    val artworkUrl100: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    val isFavorite: Boolean,
    val isInPlaylist: Boolean,
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private var currentTrackTime: String = "00:00"
    private var isPlaying: Boolean = false
    private var timerJob: Job? = null

    private var currentTrack: Track = mapArgsToTrack(trackId, trackName, artistName,
        collectionName, releaseDate, trackTime, artworkUrl100, primaryGenreName, country,
        previewUrl, isFavorite, isInPlaylist)

    private var playerStateLiveData = MutableLiveData<PlayerState>()
    fun getPlayerStateLiveData(): LiveData<PlayerState> = playerStateLiveData

    init {
        playerStateLiveData.postValue(PlayerState.Loading)

        if (previewUrl != null) {
            preparePlayer(previewUrl)
        }

        playerStateLiveData.postValue(
            PlayerState.Content(currentTrack, isPlaying, currentTrackTime)
        )
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.releasePlayer()
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        isPlaying = false
        timerJob?.cancel()
        playerStateLiveData.postValue(PlayerState.Content(currentTrack, isPlaying, currentTrackTime))
    }

    fun onFavoriteClicked() {
        if (currentTrack.isFavorite) {
            viewModelScope.launch(Dispatchers.IO) {
                playerInteractor.unlikeTrack(currentTrack)
            }
            currentTrack.isFavorite = false
            playerStateLiveData.postValue(PlayerState.Content(currentTrack, isPlaying, currentTrackTime))
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                playerInteractor.likeTrack(currentTrack)
            }
            currentTrack.isFavorite = true
            playerStateLiveData.postValue(PlayerState.Content(currentTrack, isPlaying, currentTrackTime))
        }
    }

    fun addTrackToPlaylist() {
        if (currentTrack.isInPlaylist) {
            viewModelScope.launch {
                playerInteractor.removeTrackFromPlaylist(currentTrack)
            }
            currentTrack.isInPlaylist = false
            playerStateLiveData.postValue(PlayerState.Content(currentTrack, isPlaying, currentTrackTime))

        } else {
            viewModelScope.launch {
                playerInteractor.addTrackToPlaylist(currentTrack)
            }
            currentTrack.isInPlaylist = true
            playerStateLiveData.postValue(PlayerState.Content(currentTrack, isPlaying, currentTrackTime))
        }
    }

    fun releasePlayer() {
        playerInteractor.releasePlayer()
    }

    fun playbackControl() {
        when (getPlayerState()) {
            MediaPlayerState.STATE_PLAYING -> {
                pausePlayer()
            }

            MediaPlayerState.STATE_PREPARED, MediaPlayerState.STATE_PAUSED -> {
                startPlayer()
            }

            else -> {}
        }
    }

    private fun preparePlayer(previewUrl: String) {
        playerInteractor.preparePlayer(previewUrl)
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        isPlaying = true
        currentTrackTime = getPlayerCurrentPosition()
        playerStateLiveData.postValue(PlayerState.Content(currentTrack, isPlaying, currentTrackTime))
        startTimer()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (getPlayerState() == MediaPlayerState.STATE_PLAYING) {
                delay(300L)
                currentTrackTime = getPlayerCurrentPosition()
                playerStateLiveData.postValue(
                    PlayerState.Content(currentTrack, isPlaying, currentTrackTime)
                )
            }
            if (getPlayerState() == MediaPlayerState.STATE_PREPARED) {
                pausePlayer()
                currentTrackTime = "00:00"
                playerStateLiveData.postValue(
                    PlayerState.Content(currentTrack, isPlaying, currentTrackTime)
                )
            }
        }
    }

    private fun getPlayerState(): MediaPlayerState = playerInteractor.getPlayerState()

    private fun getPlayerCurrentPosition(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(playerInteractor.getCurrentPosition()) ?: "00:00"
    }

    private fun mapArgsToTrack(trackId: Long, trackName: String?, artistName: String?,
                               collectionName: String?, releaseDate: String?, trackTime: String?,
                               artworkUrl100: String?, primaryGenreName: String?, country: String?,
                               previewUrl: String?, isFavorite: Boolean, isInPlaylist: Boolean):
            Track {
        return Track (trackId, trackName, artistName, collectionName, releaseDate, trackTime,
            artworkUrl100, primaryGenreName, country, previewUrl, isFavorite, isInPlaylist)
    }
}

