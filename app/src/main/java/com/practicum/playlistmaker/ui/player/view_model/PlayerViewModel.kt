package com.practicum.playlistmaker.ui.player.view_model

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.data.player.MediaPlayerState
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.ui.models.HandlerRouter
import com.practicum.playlistmaker.ui.models.PlayerState
import com.practicum.playlistmaker.ui.models.TrackUi
import com.practicum.playlistmaker.util.Creator
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val track: TrackUi,
    private val playerInteractor: PlayerInteractor,
    //private val trackPlayer: TrackPlayer,
    private val handlerRouter: HandlerRouter
) : ViewModel() {

    private var trackDuration: Int = 0
    private var screenStateLiveData = MutableLiveData<PlayerState>()
    fun getScreenStateLiveData(): LiveData<PlayerState> = screenStateLiveData

    private val trackProgressLiveData = MutableLiveData<Float>()
    fun getTrackProgressLiveData(): LiveData<Float> = trackProgressLiveData

    private val playButtonLiveData = MutableLiveData<Boolean>()
    fun getPlayButtonLiveData(): LiveData<Boolean> = playButtonLiveData

    private val addToPlaylistButtonLiveData = MutableLiveData<Boolean>()
    fun getAddToPlaylistButtonLiveData(): LiveData<Boolean> = addToPlaylistButtonLiveData

    private val likeButtonLiveData = MutableLiveData<Boolean>()
    fun getLikeButtonLiveData(): LiveData<Boolean> = likeButtonLiveData

    private val timerLiveData = MutableLiveData<String>()
    fun getTimerLiveData(): LiveData<String> = timerLiveData

    private val trackDurationLiveData = MutableLiveData<String>()
    fun getTrackDurationLiveData(): LiveData<String> = trackDurationLiveData

    fun preparePlayer(previewUrl: String) {
        playerInteractor.preparePlayer(previewUrl)
    }


    fun startPlayer() {
        playerInteractor.startPlayer()
        trackDuration = getTrackDuration()
        trackDurationLiveData.postValue(SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackDuration))
        playButtonLiveData.postValue(true)

        handlerRouter.startPlaying(updateTimer())

        /* На перспективу - для PlayStatus
        val statusObserver = object : TrackPlayer.StatusObserver {
            override fun onProgress(progress: Float) {
                playStatusLiveData.value = getCurrentPlayStatus().copy(progress = progress)
            }

            override fun onStop() {
                playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = false)
            }

            override fun onPlay() {
                playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = true)
            }
        }
        */
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        playButtonLiveData.postValue(false)
        handlerRouter.stopRunnable(updateTimer())
    }

    fun updateTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                var state = getPlayerState()
                if (state == MediaPlayerState.STATE_PLAYING) {
                    val currentPosition = getPlayerCurrentPosition()
                    val progress = (currentPosition.toFloat() / trackDuration.toFloat()) * 100.0f
                    timerLiveData.postValue(SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition))
                    handlerRouter.startPlaying(this)
                    trackProgressLiveData.postValue(progress)
                }

                if (state == MediaPlayerState.STATE_PREPARED) {
                    timerLiveData.postValue("0:00")
                    playButtonLiveData.postValue(false)
                    handlerRouter.stopRunnable(updateTimer())
                    trackProgressLiveData.postValue(0.0F)
                }
            }
        }
    }

    fun likeTrack() {
        if (track.isLiked) {
            playerInteractor.unlikeTrack(track.trackId)
            track.isLiked = false
            likeButtonLiveData.postValue(false)

        } else {
            playerInteractor.likeTrack(track.trackId)
            track.isLiked = true
            likeButtonLiveData.postValue(true)
        }
    }

    fun addTrackToPlaylist() {
        if (track.isInPlaylist) {
            playerInteractor.removeTrackFromPlaylist(track.trackId)
            track.isInPlaylist = false
            addToPlaylistButtonLiveData.postValue(false)

        } else {
            playerInteractor.addTrackToPlaylist(track.trackId)
            track.isInPlaylist = true
            addToPlaylistButtonLiveData.postValue(true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.releasePlayer()
        handlerRouter.stopRunnable(null)
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

    fun getPlayerState(): MediaPlayerState {
        return playerInteractor.getPlayerState()
    }

    fun getPlayerCurrentPosition(): Int {
        return playerInteractor.getCurrentPosition()
    }

    fun getTrackDuration(): Int {
        return playerInteractor.getTrackDuration()
    }

    companion object {
        fun getViewModelFactory(track: TrackUi): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as App
                PlayerViewModel(
                    track,
                    playerInteractor = Creator.providePlayerInteractor(context = application),
                    //trackPlayer = Creator.provideTrackPlayer(context = application),
                    handlerRouter = HandlerRouter(Looper.getMainLooper())
                )
            }
        }
    }

    private fun mapTrackToUi(track: Track): TrackUi {
        return TrackUi(
            track.trackName,
            track.artistName,
            track.trackId,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isLiked,
            track.isInPlaylist
        )
    }

    /* На перспективу - Playstatus и т.п.
    init {
    playerInteractor.getTrackForId(track.trackId, playerConsumer)

    playerInteractor.loadTrackData(
        trackId = track.trackId,
        onComplete = { trackUi ->
            screenStateLiveData.postValue(
                PlayerState.Content(trackUi)
            )
        }
    )

    }

    private val playStatusLiveData = MutableLiveData<PlayStatus>()
    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData


    private fun getCurrentPlayStatus(): PlayStatus {
        return playStatusLiveData.value ?: PlayStatus(progress = 0f, isPlaying = false)
    }
    */


    /* За милых дам, за милых дам. Тост на перспективу
    private val showToast = SingleLiveEvent<String>()
    fun observeToastState(): LiveData<String> = showToast

     */

    /* Ещё какая-то штука на перспективу. Разобраться
    private val playerConsumer: PlayerInteractor.PlayerInfoConsumer =
        object : PlayerInteractor.PlayerInfoConsumer {
            override fun consume(track: Track) {
                //view?.drawTrack(mapTrackToUi(track))
            }
        }

     */
}

