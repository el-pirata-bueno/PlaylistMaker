package com.practicum.playlistmaker.ui.player.activity

import android.os.Bundle
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

    private var currentTrack: TrackUi? = null
    private var previewUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        router = NavigationRouter(this)

        currentTrack = intent.getParcelableExtra<TrackUi>("track")

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory(currentTrack!!)
        )[PlayerViewModel::class.java]

        viewModel.getScreenStateLiveData().observe(this) { screenState ->
            when (screenState) {
                is PlayerState.Loading -> showContent(loading = true)
                is PlayerState.Content -> {
                    showContent(loading = false)
                    drawTrack(currentTrack!!)
                }

                else -> {}
            }
        }

        viewModel.getPlayButtonLiveData().observe(this) { isPlaying ->
            drawPlayButton(isPlaying)
        }

        viewModel.getAddToPlaylistButtonLiveData().observe(this) { isInPlaylist ->
            drawPlaylistButton(isInPlaylist)
        }

        viewModel.getLikeButtonLiveData().observe(this) { active ->
            drawLikeButton(active)
        }

        viewModel.getTrackProgressLiveData().observe(this) { trackProgress ->
            binding.trackProgressBar.progress = trackProgress.toInt()
        }

        viewModel.getTimerLiveData().observe(this) { timer ->
            binding.currentTrackTime.text = timer
        }

        viewModel.getTrackDurationLiveData().observe(this) { timer ->
            binding.totalTrackTime.text = timer
        }

        if (currentTrack != null) {
            previewUrl = currentTrack!!.previewUrl
            if (!previewUrl.equals(null)) {
                viewModel.preparePlayer(previewUrl!!)
            }
            drawTrack(currentTrack!!)
        }

        initListeners()

        /* На перспективу - PlayStatus для сикбара и т.п.
        viewModel.getPlayStatusLiveData().observe(this) { playStatus ->
            binding.trackProgressBar.progress = playStatus.progress.toInt()
        }
        */

        /* На перспективу - сообщения об ошибках
        viewModel.observeToastState().observe(this) { toast ->
            showToast(toast)
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

    private fun showContent(loading: Boolean) {
        binding.progressBar.isVisible = loading
        binding.arrowBackButton.isVisible = !loading
        binding.trackCoverBig.isVisible = !loading
        binding.trackName.isVisible = !loading
        binding.artistName.isVisible = !loading
        binding.addToPlaylistButton.isVisible = !loading
        binding.playButton.isVisible = !loading
        binding.likeButton.isVisible = !loading
        binding.currentTrackTime.isVisible = !loading
        binding.trackProgressBar.isVisible = !loading
        binding.length.isVisible = !loading
        binding.trackLength.isVisible = !loading
        binding.album.isVisible = !loading
        binding.trackAlbum.isVisible = !loading
        binding.year.isVisible = !loading
        binding.trackYear.isVisible = !loading
        binding.genre.isVisible = !loading
        binding.trackGenre.isVisible = !loading
        binding.country.isVisible = !loading
        binding.artistCountry.isVisible = !loading
    }

    private fun drawPlayButton(isPlaying: Boolean) {
        if (isPlaying) {
            binding.playButton.setImageResource(R.drawable.button_pause)
        } else {
            binding.playButton.setImageResource(R.drawable.button_play)
        }
    }

    private fun drawLikeButton(isLiked: Boolean) {
        if (isLiked) {
            binding.likeButton.setImageResource(R.drawable.button_liked)
        } else {
            binding.likeButton.setImageResource(R.drawable.button_like)
        }
    }

    private fun drawPlaylistButton(isInPlaylist: Boolean) {
        if (isInPlaylist) {
            binding.addToPlaylistButton.setImageResource(R.drawable.button_added_to_playlist)
        } else {
            binding.addToPlaylistButton.setImageResource(R.drawable.button_add_to_playlist)
        }
    }

    private fun drawTrack(track: TrackUi) {
        binding.trackLength.text = track.trackTime

        if (track.collectionName != null) {
            binding.trackAlbum.text = track.collectionName
        } else {
            binding.trackAlbum.text = ""
        }

        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackYear.text = track.releaseDate.substring(0, 4)
        binding.trackGenre.text = track.primaryGenreName
        binding.artistCountry.text = track.country

        drawLikeButton(track.isLiked)
        drawPlaylistButton(track.isInPlaylist)

        Glide.with(applicationContext)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.cover_placeholder_big)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_cover_rounded_corners)))
            .into(binding.trackCoverBig)
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

    // На перспективу - сообщения об ошибках
    //private fun showToast(message: String) {
    //    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    //}
}
