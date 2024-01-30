package com.practicum.playlistmaker.ui.media.playlists

import android.os.Bundle
import android.util.DisplayMetrics
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistPageBinding
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.media.playlists.MediaPlaylistPageState
import com.practicum.playlistmaker.presentation.media.playlists.MediaPlaylistPageViewModel
import com.practicum.playlistmaker.ui.player.PlayerFragment
import com.practicum.playlistmaker.ui.search.recycler.SearchTrackAdapter
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
private const val SCREEN_MARGINS_BOTTOM_SHEET_HEIGHT_1 = 174
private const val SCREEN_MARGINS_BOTTOM_SHEET_HEIGHT_2 = 60
class MediaPlaylistPageFragment : Fragment()  {

    private val viewModel: MediaPlaylistPageViewModel by viewModel {
        parametersOf(
            requireArguments().getInt(ARGS_PLAYLIST_ID)
        )
    }

    private var _binding: FragmentPlaylistPageBinding? = null
    private var currentPlaylist: Playlist? = null
    private var currentTracks: List<Track> = mutableListOf()
    private var currentPlaylistLength: Int = 0
    private var currentPlaylistNumTracks: Int = 0
    private val binding get() = _binding!!

    private val tracksInPlaylistAdapter = SearchTrackAdapter()

    private lateinit var bottomSheetTracksBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetMenuBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private val displayMetrics = DisplayMetrics()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce(CLICK_DEBOUNCE_DELAY_MILLIS, viewLifecycleOwner.lifecycleScope, false) { track ->
            findNavController().navigate(
                R.id.action_mediaPlaylistPageFragment_to_playerFragment, PlayerFragment.createArgs(
                    track.trackId,
                    track.trackName,
                    track.artistName,
                    track.collectionName,
                    track.releaseDate,
                    track.trackTimeMillis,
                    track.artworkUrl100,
                    track.primaryGenreName,
                    track.country,
                    track.previewUrl,
                    track.isFavorite
                )
            )
        }

        viewModel.getMediaPlaylistPageStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observeShowToast().observe(viewLifecycleOwner) {
            showToast(it)
        }

        initTracksInPlaylistAdapter()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListeners() {
        bottomSheetTracksBehavior = BottomSheetBehavior.from(binding.playlistPageBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetMenuBehavior = BottomSheetBehavior.from(binding.playlistPageMenuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.arrowBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.shareButton.setOnClickListener {
            val typeText = getString(R.string.input_text_type)
            viewModel.sharePlaylist(typeText)
        }

        binding.menuButton.setOnClickListener {
            viewModel.openMenu()
        }

        binding.sharePlaylist.setOnClickListener {
            val typeText = getString(R.string.input_text_type)
            viewModel.sharePlaylist(typeText)
        }

        binding.editPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaPlaylistPageFragment_to_mediaEditPlaylistFragment, MediaEditPlaylistFragment.createArgs(
                currentPlaylist!!.playlistId
            ))
        }

        binding.deletePlaylist.setOnClickListener {
            dialogBuilderDeletePlaylist()
        }

        bottomSheetMenuBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
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


    private fun initTracksInPlaylistAdapter() {
        tracksInPlaylistAdapter.itemClickListener = { track ->
            onTrackClickDebounce(track)
        }

        tracksInPlaylistAdapter.itemLongClickListener = { track ->
            dialogBuilderDeleteTrack(track)
        }

        binding.tracksInPlaylistRecycler.adapter = tracksInPlaylistAdapter
        binding.tracksInPlaylistRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }


    private fun dialogBuilderDeleteTrack(track: Track) {
        MaterialAlertDialogBuilder(requireContext(), R.style.Theme_MyApp_Dialog_Alert)
            .setTitle(R.string.dialog_title_delete_track_from_playlist)
            .setMessage(R.string.dialog_message_delete_track_from_playlist)
            .setNeutralButton(R.string.dialog_negative_button_delete_track_from_playlist) { _, _ ->
            }
            .setPositiveButton(R.string.dialog_positive_button_delete_track_from_playlist) { _, _ ->
                viewModel.deleteTrackFromPlaylist(track)
                tracksInPlaylistAdapter.notifyDataSetChanged()
            }
            .show()
    }

    private fun dialogBuilderDeletePlaylist() {
        MaterialAlertDialogBuilder(requireContext(), R.style.Theme_MyApp_Dialog_Alert)
            .setTitle(R.string.dialog_title_delete_playlist)
            .setMessage(String.format(getString(R.string.dialog_message_delete_playlist), currentPlaylist!!.name))
            .setNeutralButton(R.string.dialog_negative_button_delete_playlist) { dialog, which ->
            }
            .setPositiveButton(R.string.dialog_positive_button_delete_playlist) { dialog, which ->
                viewModel.deletePlaylist()
                findNavController().navigateUp()
            }
            .show()
    }

    private fun render(state: MediaPlaylistPageState) {
        when (state) {
            is MediaPlaylistPageState.PreLoading -> showPreLoading()
            is MediaPlaylistPageState.Content -> showContent(state.playlist, state.tracks, state.playlistLength)
            is MediaPlaylistPageState.MenuBottomSheet -> showContentWithMenuBottomSheet()
        }
    }
    private fun showPreLoading() {
        bottomSheetTracksBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        drawPlaylist(null, currentPlaylistLength)
    }

    private fun showContent(playlist: Playlist?, tracks: List<Track>, playlistLength: Int) {
        currentPlaylist = playlist
        currentTracks = tracks
        currentPlaylistLength = playlistLength
        currentPlaylistNumTracks = playlist?.numTracks ?: 0

        drawPlaylist(currentPlaylist, currentPlaylistLength)

        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

        val bottomSheetTracksHeight = displayMetrics.heightPixels - (displayMetrics.widthPixels +
                (SCREEN_MARGINS_BOTTOM_SHEET_HEIGHT_1 * displayMetrics.density).toInt())

        bottomSheetTracksBehavior.peekHeight = bottomSheetTracksHeight

        if (tracks.isNotEmpty()) {
            tracksInPlaylistAdapter.tracks.clear()
            tracksInPlaylistAdapter.tracks.addAll(tracks)
            tracksInPlaylistAdapter.notifyDataSetChanged()
            binding.playlistPageBottomSheet.isVisible = true
        }
        else {
            binding.playlistPageBottomSheet.isVisible = false
        }
        bottomSheetMenuBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun showContentWithMenuBottomSheet() {
        drawPlaylist(currentPlaylist, currentPlaylistLength)

        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val bottomSheetMenuHeight = displayMetrics.heightPixels - (displayMetrics.widthPixels +
                (SCREEN_MARGINS_BOTTOM_SHEET_HEIGHT_2 * displayMetrics.density).toInt())

        bottomSheetMenuBehavior.peekHeight = bottomSheetMenuHeight
        bottomSheetMenuBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val roundedCorner = requireContext()
            .resources.getDimensionPixelSize(R.dimen.tracklist_cover_rounded_corners)

        Glide.with(requireContext())
            .load(currentPlaylist!!.imageLink)
            .placeholder(R.drawable.cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(roundedCorner))
            .into(binding.playlistImage)

        binding.playlistNameMenu.text = currentPlaylist!!.name
        binding.tracksNumber.text = resources.getQuantityString(R.plurals.plurals_track, currentPlaylist!!.numTracks, currentPlaylist!!.numTracks)
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun drawPlaylist (playlist: Playlist?, playlistLength: Int) {
        if (playlist != null) {
            Glide.with(requireContext())
                .load(playlist.imageLink)
                .placeholder(R.drawable.cover_placeholder_big)
                .centerCrop()
                .into(binding.trackCoverBig)

            binding.playlistName.isVisible = true
            binding.playlistDescription.isVisible = true
            binding.playlistName.text = playlist.name
            binding.playlistDescription.text = playlist.description
            binding.playlistLength.text = resources.getQuantityString(R.plurals.plurals_minute, playlistLength, playlistLength)
            binding.playlistNumTracks.text = resources.getQuantityString(R.plurals.plurals_track, playlist.numTracks, playlist.numTracks)
        }
        else {
            binding.playlistName.isVisible = false
            binding.playlistDescription.isVisible = false
            binding.playlistLength.text = resources.getQuantityString(R.plurals.plurals_minute, currentPlaylistLength, currentPlaylistLength)
            binding.playlistNumTracks.text = resources.getQuantityString(R.plurals.plurals_track, currentPlaylistNumTracks, currentPlaylistNumTracks)
        }
    }

    companion object {
        private const val ARGS_PLAYLIST_ID = "playlistId"

        fun createArgs(playlistId: Int): Bundle =
            bundleOf(
                ARGS_PLAYLIST_ID to playlistId
            )
    }
}