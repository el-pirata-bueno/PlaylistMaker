package com.practicum.playlistmaker.presentation.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.data.player.MediaPlayerState
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    trackId: Long,
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private lateinit var track: Track
    private var currentTrackTime: String = "00:00"
    private var isPlaying: Boolean = false
    private var timerJob: Job? = null

    private var playerStateLiveData = MutableLiveData<PlayerState>()
    fun getPlayerStateLiveData(): LiveData<PlayerState> = playerStateLiveData

    init {
        playerStateLiveData.postValue(PlayerState.Loading)

        //if (trackId != 0) {

        trackId.let {
            viewModelScope.launch {
                playerInteractor
                    .getTrackFromId(trackId)
                    .collect { pair ->
                        getTrackResult(pair.first, pair.second)
                    }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.releasePlayer()
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        isPlaying = false
        timerJob?.cancel()
        playerStateLiveData.postValue(PlayerState.Content(track, isPlaying, currentTrackTime))
    }

    fun onFavoriteClicked() {
        if (track.isFavorite) {
            viewModelScope.launch {
                playerInteractor.unlikeTrack(track)
            }
            track.isFavorite = false
            playerStateLiveData.postValue(PlayerState.Content(track, isPlaying, currentTrackTime))
        } else {

            playerInteractor.likeTrack(track)
            track.isFavorite = true
            playerStateLiveData.postValue(PlayerState.Content(track, isPlaying, currentTrackTime))
        }
    }

    fun addTrackToPlaylist() {
        if (track.isInPlaylist) {
            viewModelScope.launch {
                playerInteractor.removeTrackFromPlaylist(track.trackId)
            }
            track.isInPlaylist = false
            playerStateLiveData.postValue(PlayerState.Content(track, isPlaying, currentTrackTime))

        } else {
            viewModelScope.launch {
                playerInteractor.addTrackToPlaylist(track.trackId)
            }
            track.isInPlaylist = true
            playerStateLiveData.postValue(PlayerState.Content(track, isPlaying, currentTrackTime))
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

    private fun getTrackResult(foundTracks: List<Track>?, errorMessage: String?) {
        if (foundTracks != null) {
            var tracks = foundTracks.toMutableList()
            track = tracks[0]
            if (track.previewUrl != null) {
                preparePlayer(track.previewUrl!!)
            }
            playerStateLiveData.postValue(
                PlayerState.Content(
                    track, isPlaying, currentTrackTime
                )
            )
        } else {
            if (errorMessage != null) playerStateLiveData.postValue(
                PlayerState.Error(
                    errorMessage
                )
            )
        }
    }

    private fun preparePlayer(previewUrl: String) {
        playerInteractor.preparePlayer(previewUrl)
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()

        isPlaying = true
        currentTrackTime = getPlayerCurrentPosition()
        playerStateLiveData.postValue(PlayerState.Content(track, isPlaying, currentTrackTime))
        startTimer()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (getPlayerState() == MediaPlayerState.STATE_PLAYING) {
                delay(300L)
                currentTrackTime = getPlayerCurrentPosition()
                playerStateLiveData.postValue(
                    PlayerState.Content(
                        track,
                        isPlaying,
                        currentTrackTime
                    )
                )
            }
            if (getPlayerState() == MediaPlayerState.STATE_PREPARED) {
                pausePlayer()
                currentTrackTime = "00:00"
                playerStateLiveData.postValue(
                    PlayerState.Content(
                        track,
                        isPlaying,
                        currentTrackTime
                    )
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
}

