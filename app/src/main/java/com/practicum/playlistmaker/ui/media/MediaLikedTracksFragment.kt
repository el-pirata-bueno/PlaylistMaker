package com.practicum.playlistmaker.ui.media

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentLikedTracksBinding
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.media.MediaLikedTracksState
import com.practicum.playlistmaker.presentation.media.MediaLikedTracksViewModel
import com.practicum.playlistmaker.ui.player.PlayerFragment
import com.practicum.playlistmaker.ui.search.SearchTrackAdapter
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaLikedTracksFragment: Fragment() {

    private val viewModel by viewModel<MediaLikedTracksViewModel>()

    private var _binding: FragmentLikedTracksBinding? = null

    private val binding get() = _binding!!
    private var likedTracksAdapter: SearchTrackAdapter? = null
    private lateinit var onTrackClickDebounce: (Track) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikedTracksBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
            findNavController().navigate(R.id.action_mediaFragment_to_playerFragment, PlayerFragment.createArgs(
                track.trackId,
                track.trackName,
                track.artistName,
                track.collectionName,
                track.releaseDate,
                track.trackTime,
                track.artworkUrl100,
                track.primaryGenreName,
                track.country,
                track.previewUrl,
                track.isFavorite
                )
            )
        }

        initLikedTracksAdapter()

        viewModel.fillData()

        viewModel.getMediaLikedStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
        viewModel.getMediaLikedStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun initLikedTracksAdapter() {
        likedTracksAdapter = SearchTrackAdapter()
        likedTracksAdapter!!.itemClickListener = { track ->
            onTrackClickDebounce(track)
            Log.d("ISFAVORITE","$track.isFavorite")
        }

        binding.likedTracksRecycler.adapter = likedTracksAdapter
        binding.likedTracksRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun render(state: MediaLikedTracksState) {
        when (state) {
            is MediaLikedTracksState.Content -> showContent(state.tracks)
            is MediaLikedTracksState.Empty -> showEmpty()
        }
    }

    private fun showEmpty() {
        binding.placeholderImage.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.likedTracksRecycler.visibility = View.GONE
    }

    private fun showContent(tracks: List<Track>) {
        binding.placeholderImage.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.likedTracksRecycler.visibility = View.VISIBLE

        likedTracksAdapter?.tracks?.clear()
        likedTracksAdapter?.tracks?.addAll(tracks)
        likedTracksAdapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        likedTracksAdapter = null
        _binding = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun newInstance() = MediaLikedTracksFragment().apply { }
    }

}