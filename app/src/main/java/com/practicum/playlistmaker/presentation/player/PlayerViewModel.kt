package com.practicum.playlistmaker.presentation.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.data.player.MediaPlayerState
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.ui.models.HandlerRouter
import com.practicum.playlistmaker.ui.models.TrackUi
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val trackId: Int,
    private val playerInteractor: PlayerInteractor,
    private val handlerRouter: HandlerRouter
) : ViewModel() {

    private lateinit var track: TrackUi
    private var trackDuration: Int = 0
    private var currentTrackTime: String = "0:00"
    private var isPlaying: Boolean = false

    private var playerStateLiveData = MutableLiveData<PlayerState>()
    fun getPlayerStateLiveData(): LiveData<PlayerState> = playerStateLiveData

    init {
        playerStateLiveData.postValue(PlayerState.Loading)

        if (trackId != 0) {
            playerInteractor.getTrackFromId(trackId,
                object : PlayerInteractor.GetTrackFromIdConsumer {
                    override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                        if (foundTracks != null) {
                            var tracks = foundTracks.toMutableList()
                            track = mapTrackToUi(tracks[0])
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
                })
        }
    }

    fun preparePlayer(previewUrl: String) {
        playerInteractor.preparePlayer(previewUrl)
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        trackDuration = getTrackDuration()
        isPlaying = true
        playerStateLiveData.postValue(PlayerState.Content(track, isPlaying, currentTrackTime))
        handlerRouter.startPlaying(updateTimer())

        //trackDurationLiveData.postValue(SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackDuration))
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        isPlaying = false
        playerStateLiveData.postValue(PlayerState.Content(track, isPlaying, currentTrackTime))
        handlerRouter.stopRunnable(updateTimer())
    }

    fun updateTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                var state = getPlayerState()
                if (state == MediaPlayerState.STATE_PLAYING) {
                    val currentPosition = getPlayerCurrentPosition()
                    //val progress = (currentPosition.toFloat() / trackDuration.toFloat()) * 100.0f
                    currentTrackTime =
                        SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition)
                    playerStateLiveData.postValue(
                        PlayerState.Content(track, isPlaying, currentTrackTime)
                    )
                    handlerRouter.startPlaying(this)

                    //trackProgressLiveData.postValue(progress)
                }

                if (state == MediaPlayerState.STATE_PREPARED) {
                    isPlaying = false
                    currentTrackTime = "0:00"
                    playerStateLiveData.postValue(
                        PlayerState.Content(
                            track, isPlaying, currentTrackTime
                        )
                    )
                    handlerRouter.stopRunnable(updateTimer())

                    //trackProgressLiveData.postValue(0.0F)
                }
            }
        }
    }

    fun likeTrack() {
        if (track.isLiked) {
            playerInteractor.unlikeTrack(track.trackId)
            track.isLiked = false
            playerStateLiveData.postValue(PlayerState.Content(track, isPlaying, currentTrackTime))
        } else {
            playerInteractor.likeTrack(track.trackId)
            track.isLiked = true
            playerStateLiveData.postValue(PlayerState.Content(track, isPlaying, currentTrackTime))
        }
    }

    fun addTrackToPlaylist() {
        if (track.isInPlaylist) {
            playerInteractor.removeTrackFromPlaylist(track.trackId)
            track.isInPlaylist = false
            playerStateLiveData.postValue(PlayerState.Content(track, isPlaying, currentTrackTime))

        } else {
            playerInteractor.addTrackToPlaylist(track.trackId)
            track.isInPlaylist = true
            playerStateLiveData.postValue(PlayerState.Content(track, isPlaying, currentTrackTime))
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

    fun getPlayerState(): MediaPlayerState = playerInteractor.getPlayerState()
    fun getPlayerCurrentPosition(): Int = playerInteractor.getCurrentPosition()
    fun getTrackDuration(): Int = playerInteractor.getTrackDuration()
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

}

