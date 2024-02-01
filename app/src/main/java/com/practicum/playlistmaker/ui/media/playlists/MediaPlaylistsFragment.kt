package com.practicum.playlistmaker.ui.media.playlists

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.presentation.media.playlists.MediaPlaylistsState
import com.practicum.playlistmaker.presentation.media.playlists.MediaPlaylistsViewModel
import com.practicum.playlistmaker.ui.media.playlists.recycler.MediaPlaylistsGridAdapter
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
class MediaPlaylistsFragment: Fragment()  {

    private val viewModel by viewModel<MediaPlaylistsViewModel>()

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val playlistsAdapter = MediaPlaylistsGridAdapter()

    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        onPlaylistClickDebounce = debounce(CLICK_DEBOUNCE_DELAY_MILLIS, viewLifecycleOwner.lifecycleScope, false) { playlist ->
            findNavController().navigate(R.id.action_mediaFragment_to_mediaPlaylistPageFragment, MediaPlaylistPageFragment.createArgs(
                playlist.playlistId
                )
            )
        }

        initPlaylistAdapter()
        initListeners()

        viewModel.fillData()

        viewModel.getMediaPlaylistsStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
        viewModel.getMediaPlaylistsStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun initListeners() {
        binding.addNewPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_newPlaylistFragment, MediaNewPlaylistFragment.createArgs(
                null
            ))
        }
    }

    private fun initPlaylistAdapter() {
        binding.playlistsGridRecycler.adapter = playlistsAdapter
        binding.playlistsGridRecycler.layoutManager =
            GridLayoutManager(requireContext(), 2)
        val itemDecoration = ItemOffsetDecoration(requireContext(), R.dimen.item_offset)
        binding.playlistsGridRecycler.addItemDecoration(itemDecoration)

        playlistsAdapter.itemClickListener = { playlist ->
            onPlaylistClickDebounce(playlist)
        }
    }

    private fun render(state: MediaPlaylistsState) {
        when (state) {
            is MediaPlaylistsState.Content -> showContent(state.playlists)
            is MediaPlaylistsState.Empty -> showEmpty()
        }
    }

    private fun showEmpty() {
        binding.placeholderImage.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.playlistsGridRecycler.visibility = View.GONE
    }

    private fun showContent(playlists: List<Playlist>) {
        binding.placeholderImage.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.playlistsGridRecycler.visibility = View.VISIBLE

        playlistsAdapter?.playlists?.clear()
        playlistsAdapter?.playlists?.addAll(playlists)
        playlistsAdapter?.notifyDataSetChanged()
    }

    companion object {
        fun newInstance() = MediaPlaylistsFragment().apply { }
    }
}


class ItemOffsetDecoration(private val mItemOffset: Int) : ItemDecoration() {
    constructor(
        context: Context,
        @DimenRes itemOffsetId: Int
    ) : this(context.resources.getDimensionPixelSize(itemOffsetId))

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(mItemOffset, 0, mItemOffset, mItemOffset+12)
    }
}