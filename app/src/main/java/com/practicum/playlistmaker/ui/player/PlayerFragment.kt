package com.practicum.playlistmaker.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.player.PlayerState
import com.practicum.playlistmaker.presentation.player.PlayerViewModel
import com.practicum.playlistmaker.ui.media.playlists.MediaNewPlaylistFragment
import com.practicum.playlistmaker.ui.media.playlists.recycler.MediaPlaylistsListAdapter
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
class PlayerFragment: Fragment() {

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(
            requireArguments().getLong(ARGS_TRACK_ID),
            requireArguments().getString(ARGS_TRACK_NAME),
            requireArguments().getString(ARGS_ARTIST_NAME),
            requireArguments().getString(ARGS_COLLECTION_NAME),
            requireArguments().getString(ARGS_RELEASE_DATE),
            requireArguments().getInt(ARGS_TRACK_TIME),
            requireArguments().getString(ARGS_ARTWORK_URL),
            requireArguments().getString(ARGS_GENRE_NAME),
            requireArguments().getString(ARGS_COUNTRY),
            requireArguments().getString(ARGS_PREVIEW_URL),
            if (ARGS_IS_FAVORITE != null) requireArguments().getBoolean(ARGS_IS_FAVORITE) else false
            )
    }

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val playlistsAdapter = MediaPlaylistsListAdapter()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onPlaylistClickDebounce = debounce(CLICK_DEBOUNCE_DELAY_MILLIS, viewLifecycleOwner.lifecycleScope, false) { playlist ->
            viewModel.onPlaylistClicked(playlist)
        }

        viewModel.getPlayerStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observeShowToast().observe(viewLifecycleOwner) {
            showToast(it)
        }

        initPlaylistAdapter()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: PlayerState) {
        when (state) {
            is PlayerState.Error -> showError(getString(R.string.track_error))
            is PlayerState.Player -> {
                showPlayer()
                drawTrack(state.track, state.isPlaying,
                    state.currentTrackTime)
            }
            is PlayerState.PlayerWithBottomSheet -> {
                showPlayer()
                showContentWithBottomSheet(state.playlists)
                drawTrack(state.track, state.isPlaying, state.currentTrackTime)
            }
            else -> {}
        }
    }

    private fun showPlayer() {
        binding.playerContent.isVisible = true
        binding.playerBottomSheet.isVisible = false
    }

    private fun showContentWithBottomSheet(playlists: List<Playlist>) {
        binding.playerContent.isVisible = true
        binding.playerBottomSheet.isVisible = true

        playlistsAdapter.playlists.clear()
        playlistsAdapter.playlists.addAll(playlists)
        playlistsAdapter.notifyDataSetChanged()
    }

    private fun showError(errorMessage: String) {
        binding.arrowBackButton.isVisible = true
        binding.progressBar.isVisible = true
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun drawTrack(track: Track, isPlaying: Boolean, currentTrackTime: String) {

        Glide.with(requireContext())
            .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.cover_placeholder_big)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_cover_rounded_corners)))
            .into(binding.trackCoverBig)

        binding.playButton.setImageResource(if (isPlaying) R.drawable.button_pause else R.drawable.button_play)
        binding.likeButton.setImageResource(if (track.isFavorite) R.drawable.button_liked else R.drawable.button_like)

        binding.currentTrackTime.text = currentTrackTime

        if (track.trackTimeMillis != null) {
            binding.trackLength.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        }
        else {
            binding.trackLength.text = "00:00"
        }

        binding.trackAlbum.text = track.collectionName ?: ""
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackYear.text = track.releaseDate?.substring(0, 4) ?: ""
        binding.trackGenre.text = track.primaryGenreName
        binding.artistCountry.text = track.country
    }

    private fun initListeners() {

        bottomSheetBehavior = BottomSheetBehavior.from(binding.playerBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.arrowBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.addToPlaylistButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            viewModel.onAddToPlaylistClicked()
        }

        binding.likeButton.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        binding.addNewPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment, MediaNewPlaylistFragment.createArgs(
                null
            ))
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
            }
        )


    }

    private fun initPlaylistAdapter() {
        playlistsAdapter.itemClickListener = { playlist ->
            onPlaylistClickDebounce(playlist)
            playlistsAdapter.notifyDataSetChanged()
        }

        binding.playlistsListRecycler.adapter = playlistsAdapter
        binding.playlistsListRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    companion object {
        private const val ARGS_TRACK_ID = "trackId"
        private const val ARGS_TRACK_NAME = "trackName"
        private const val ARGS_ARTIST_NAME = "artistName"
        private const val ARGS_COLLECTION_NAME = "collectionName"
        private const val ARGS_RELEASE_DATE = "releaseDate"
        private const val ARGS_TRACK_TIME = "trackTime"
        private const val ARGS_ARTWORK_URL = "artworkUrl"
        private const val ARGS_GENRE_NAME = "genreName"
        private const val ARGS_COUNTRY = "country"
        private const val ARGS_PREVIEW_URL = "previewUrl"
        private const val ARGS_IS_FAVORITE = "isFavorite"

        fun createArgs(trackId: Long, trackName: String?, artistName: String?,
                       collectionName: String?, releaseDate: String?, trackTimeMillis: Int?,
                       artworkUrl100: String?, primaryGenreName: String?, country: String?,
                       previewUrl: String?, isFavorite: Boolean?): Bundle =
            bundleOf(
                ARGS_TRACK_ID to trackId,
                ARGS_TRACK_NAME to trackName,
                ARGS_ARTIST_NAME to artistName,
                ARGS_COLLECTION_NAME to collectionName,
                ARGS_RELEASE_DATE to releaseDate,
                ARGS_TRACK_TIME to trackTimeMillis,
                ARGS_ARTWORK_URL to artworkUrl100,
                ARGS_GENRE_NAME to primaryGenreName,
                ARGS_COUNTRY to country,
                ARGS_PREVIEW_URL to previewUrl,
                ARGS_IS_FAVORITE to isFavorite
                )
    }
}