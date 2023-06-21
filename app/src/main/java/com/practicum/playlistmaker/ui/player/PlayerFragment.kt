package com.practicum.playlistmaker.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.presentation.player.PlayerState
import com.practicum.playlistmaker.presentation.player.PlayerViewModel
import com.practicum.playlistmaker.ui.models.TrackUi
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment: Fragment() {

    companion object {
        const val TRACK_ID = "track_id"

        fun createArgs(track_id: Int): Bundle =
            bundleOf(TRACK_ID to track_id)
    }

    private val viewModel: PlayerViewModel by viewModel()
    {
        parametersOf(requireArguments().getInt(TRACK_ID))
    }

    private lateinit var binding: FragmentPlayerBinding
    private var errorText = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        errorText = getString(R.string.track_error)

        val trackId = requireArguments().getInt(TRACK_ID)

        viewModel.getPlayerStateLiveData().observe(viewLifecycleOwner) { screenState ->
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
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun drawTrack(track: TrackUi, isPlaying: Boolean, currentTrackTime: String) {

        Glide.with(requireContext())
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
            //router.goBack()
            findNavController().popBackStack()
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