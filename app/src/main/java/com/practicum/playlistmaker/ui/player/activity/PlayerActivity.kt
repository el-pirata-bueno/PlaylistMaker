package com.practicum.playlistmaker.ui.player.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.ui.models.NavigationRouter
import com.practicum.playlistmaker.ui.models.PlayerState
import com.practicum.playlistmaker.ui.models.TrackUi
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel

class PlayerActivity : AppCompatActivity() {
    private lateinit var viewModel: PlayerViewModel
    private lateinit var router: NavigationRouter
    private lateinit var binding: ActivityPlayerBinding
    private var errorText = ""

    private var trackId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        errorText = getString(R.string.track_error)

        router = NavigationRouter(this)
        trackId = intent.getIntExtra("trackId", 0)

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory(trackId)
        )[PlayerViewModel::class.java]

        viewModel.getPlayerStateLiveData().observe(this) { screenState ->
            when (screenState) {
                is PlayerState.Error -> showError(errorText)
                is PlayerState.Content -> {
                    showContent()
                    drawTrack(screenState.track, screenState.isPlaying, screenState.currentTrackTime)
                }
                else -> {}
            }
        }

        initListeners()

        /* Для реализации расширенного функционала - фактическая длительность трека + прогерсс-бар
        viewModel.getTrackProgressLiveData().observe(this) { trackProgress ->
            binding.trackProgressBar.progress = trackProgress.toInt()
        }

        viewModel.getTrackDurationLiveData().observe(this) { timer ->
            binding.totalTrackTime.text = timer
        }
        */
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }

    private fun showContent() {
        binding.arrowBackButton.isVisible = true
        binding.progressBar.isVisible = false
        binding.trackCoverBig.isVisible = true
        binding.trackName.isVisible = true
        binding.artistName.isVisible = true
        binding.addToPlaylistButton.isVisible = true
        binding.playButton.isVisible = true
        binding.likeButton.isVisible = true
        binding.currentTrackTime.isVisible = true
        binding.length.isVisible = true
        binding.trackLength.isVisible = true
        binding.album.isVisible = true
        binding.trackAlbum.isVisible = true
        binding.year.isVisible = true
        binding.trackYear.isVisible = true
        binding.genre.isVisible = true
        binding.trackGenre.isVisible = true
        binding.country.isVisible = true
        binding.artistCountry.isVisible = true
    }

    private fun showError(errorMessage: String) {
        binding.arrowBackButton.isVisible = true
        binding.progressBar.isVisible = true
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun drawTrack(track: TrackUi, isPlaying: Boolean, currentTrackTime: String) {

        Glide.with(applicationContext)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.cover_placeholder_big)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_cover_rounded_corners)))
            .into(binding.trackCoverBig)

        binding.playButton.setImageResource(if (isPlaying) R.drawable.button_pause else R.drawable.button_play)
        binding.likeButton.setImageResource(if (track.isLiked) R.drawable.button_liked else R.drawable.button_like)
        binding.addToPlaylistButton.setImageResource(if (track.isInPlaylist) R.drawable.button_added_to_playlist else R.drawable.button_add_to_playlist)

        binding.currentTrackTime.text = currentTrackTime

        binding.trackLength.text = track.trackTime
        binding.trackAlbum.text = track.collectionName ?: ""
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackYear.text = track.releaseDate.substring(0, 4)
        binding.trackGenre.text = track.primaryGenreName
        binding.artistCountry.text = track.country
    }

    private fun initListeners() {
        binding.arrowBackButton.setOnClickListener {
            router.goBack()
        }

        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.addToPlaylistButton.setOnClickListener {
            viewModel.addTrackToPlaylist()
        }

        binding.likeButton.setOnClickListener {
            viewModel.likeTrack()
        }
    }
}
