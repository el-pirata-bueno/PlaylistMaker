package com.practicum.playlistmaker.presentation.player

import com.practicum.playlistmaker.data.player.PlayerState
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.models.TrackUi

class PlayerPresenter(
    private var view: PlayerScreenView?,
    private val trackId: String,
    private val playerInteractor: PlayerInteractor
) {

    companion object {
        private val MP_DELAY = 1000L
    }

    var addedToPlaylist = false
    var addedToFavourites = false

    private val playerConsumer: PlayerInteractor.PlayerInfoConsumer =
        object : PlayerInteractor.PlayerInfoConsumer {
            override fun consume(track: Track) {
                view?.drawTrack(mapTrackToUi(track))
            }
        }

    init {
        playerInteractor.getTrackForId(trackId, playerConsumer)
    }

    fun arrowBackButtonClicked() {
        view?.goBack()
    }

    fun onViewDestroyed() {
        view = null
        playerInteractor.releasePlayer()
    }

    fun onPlayButtonClicked() {
        playbackControl()
    }

    fun onAddToPlaylistButtonClicked() {
        addedToPlaylist = !addedToPlaylist
        view?.addedToPlaylist(addedToPlaylist)
        playerInteractor.addTrackToPlaylist(trackId, playerConsumer)
    }

    fun onAddToFavouritesButtonClicked() {
        addedToFavourites = !addedToFavourites
        view?.addedToFavourites(addedToFavourites)
        playerInteractor.likeTrack(trackId, playerConsumer)
    }

    fun preparePlayer(trackUrl: String) {
            playerInteractor.preparePlayer(trackUrl)
    }

    fun startPlayer() {
        playerInteractor.startPlayer()
        if (getPlayerState() == PlayerState.STATE_PLAYING) {
            view?.startedTrack()
        }
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        if (getPlayerState() == PlayerState.STATE_PAUSED) {
            view?.pausedTrack()
        }
    }

    fun playbackControl() {
        when (getPlayerState()) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
                view?.startTimer()
            }
            else -> {}
        }
    }

    fun getPlayerState(): PlayerState  {
        return playerInteractor.getPlayerState()
    }

    fun getPlayerCurrentPosition(): Int  {
        return playerInteractor.getCurrentPosition()
    }

    private fun mapTrackToUi(track: Track): TrackUi {
        return TrackUi(
            track.trackName,
            track.artistName,
            track.trackId,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isLiked,
            track.isFavourite
        )
    }
}