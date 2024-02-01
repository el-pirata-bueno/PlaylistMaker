package com.practicum.playlistmaker.presentation.player

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.player.MediaPlayerState
import com.practicum.playlistmaker.domain.media.MediaPlaylistsInteractor
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.ui.model.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

private const val UPDATE_PLAYLISTS_DELAY_TIME_MILLIS = 200L
private const val UPDATE_PLAYER_DELAY_TIME_MILLIS = 300L
class PlayerViewModel(
    val trackId: Long,
    val trackName: String?,
    val artistName: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val trackTimeMillis: Int?,
    val artworkUrl100: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    val isFavorite: Boolean,
    application: Application,
    private val playerInteractor: PlayerInteractor,
    private val mediaPlaylistsInteractor: MediaPlaylistsInteractor,
    ) : AndroidViewModel(application) {

    private var currentTrackTime: String = "00:00"
    private var isPlaying: Boolean = false
    private var timerJob: Job? = null

    private var currentTrack: Track = mapArgsToTrack(trackId, trackName, artistName,
        collectionName, releaseDate, trackTimeMillis, artworkUrl100, primaryGenreName, country,
        previewUrl, isFavorite)
    private var listPlaylists: MutableList<Playlist> = mutableListOf()

    private var playerStateLiveData = MutableLiveData<PlayerState>()
    fun getPlayerStateLiveData(): LiveData<PlayerState> = playerStateLiveData

    private val showToast = SingleLiveEvent<String?>()
    fun observeShowToast(): LiveData<String?> = showToast

    init {
        playerStateLiveData.postValue(PlayerState.Loading)

        if (previewUrl != null) {
            preparePlayer(previewUrl)
        }

        playerStateLiveData.postValue(
            PlayerState.Player(listPlaylists, currentTrack, isPlaying, currentTrackTime)
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
        playerStateLiveData.postValue(PlayerState.Player(listPlaylists, currentTrack, isPlaying, currentTrackTime))
    }

    fun onFavoriteClicked() {
        if (currentTrack.isFavorite) {
            viewModelScope.launch(Dispatchers.IO) {
                playerInteractor.unlikeTrack(currentTrack)
            }
            currentTrack.isFavorite = false
            playerStateLiveData.postValue(PlayerState.Player(listPlaylists, currentTrack, isPlaying, currentTrackTime))
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                playerInteractor.likeTrack(currentTrack)
            }
            currentTrack.isFavorite = true
            playerStateLiveData.postValue(PlayerState.Player(listPlaylists, currentTrack, isPlaying, currentTrackTime))
        }
    }

    fun onAddToPlaylistClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(UPDATE_PLAYLISTS_DELAY_TIME_MILLIS)
            mediaPlaylistsInteractor
                .getPlaylists()
                .collect {playlists ->
                    listPlaylists.clear()
                    listPlaylists.addAll(playlists)
                    renderState(PlayerState.Player(listPlaylists, currentTrack, isPlaying, currentTrackTime))
                }
        }
    }

    fun onPlaylistClicked(playlist: Playlist) {
        val trackIds = playlist.listTracks
        var trackAdded = false
        if (trackIds.isNotEmpty()) {
            for (trackId in trackIds) {
                if (currentTrack.trackId == trackId) {
                    val toastText = getApplication<Application>().resources.getString(R.string.toast_already_added_to_playlist, playlist.name)
                    showToast.postValue(toastText)
                    trackAdded = true
                    break
                }
            }
            if (!trackAdded) addToPlaylist(playlist)
        }
        else {
            addToPlaylist(playlist)
        }
    }

    private fun addToPlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            mediaPlaylistsInteractor.insertTrackInPlaylist(currentTrack, playlist)
        }
        val toastText = getApplication<Application>().resources.getString(R.string.toast_added_to_playlist, playlist.name)
        showToast.postValue(toastText)
        onAddToPlaylistClicked()
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
        playerStateLiveData.postValue(PlayerState.Player(listPlaylists, currentTrack, isPlaying, currentTrackTime))
        startTimer()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (getPlayerState() == MediaPlayerState.STATE_PLAYING) {
                delay(UPDATE_PLAYER_DELAY_TIME_MILLIS)
                currentTrackTime = getPlayerCurrentPosition()
                playerStateLiveData.postValue(
                    PlayerState.Player(listPlaylists, currentTrack, isPlaying, currentTrackTime)
                )
            }
            if (getPlayerState() == MediaPlayerState.STATE_PREPARED) {
                pausePlayer()
                currentTrackTime = "00:00"
                playerStateLiveData.postValue(
                    PlayerState.Player(listPlaylists, currentTrack, isPlaying, currentTrackTime)
                )
            }
        }
    }

    private fun getPlayerState(): MediaPlayerState = playerInteractor.getPlayerState()

    private fun renderState(state: PlayerState) {
        playerStateLiveData.postValue(state)
    }

    private fun getPlayerCurrentPosition(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(playerInteractor.getCurrentPosition()) ?: "00:00"
    }

    private fun mapArgsToTrack(trackId: Long, trackName: String?, artistName: String?,
                               collectionName: String?, releaseDate: String?, trackTimeMillis: Int?,
                               artworkUrl100: String?, primaryGenreName: String?, country: String?,
                               previewUrl: String?, isFavorite: Boolean):
            Track {
        return Track (trackId, trackName, artistName, collectionName, releaseDate, trackTimeMillis,
            artworkUrl100, primaryGenreName, country, previewUrl, isFavorite)
    }
}

