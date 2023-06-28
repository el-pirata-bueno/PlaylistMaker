package com.practicum.playlistmaker.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.presentation.search.SearchState
import com.practicum.playlistmaker.presentation.search.SearchViewModel
import com.practicum.playlistmaker.ui.models.TrackUi
import com.practicum.playlistmaker.ui.player.PlayerFragment
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment: Fragment() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModel()

    private val trackAdapter = TrackAdapter()
    private var historyAdapter = TrackAdapter()
    private lateinit var searchText: String
    private var message = ""

    private lateinit var onTrackClickDebounce: (TrackUi) -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.inputSearch.setText(savedInstanceState?.getString("SEARCH_TEXT", ""))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce<TrackUi>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
                findNavController().navigate(R.id.action_searchFragment_to_playerFragment, PlayerFragment.createArgs(track.trackId))
        }



        message = getString(R.string.nothing_found)

        viewModel.getSearchStateLiveData().observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is SearchState.Content -> showContent(screenState.tracks)
                is SearchState.History -> showHistory(screenState.historyTracks, screenState.clearSearch)
                is SearchState.Empty -> showEmpty()
                is SearchState.Error -> showError(screenState.errorMessage)
                is SearchState.Loading -> showLoading()
                is SearchState.PreLoading -> showPreLoading(screenState.buttonVisible)
                else -> {}
            }
        }
        viewModel.changeText(binding.inputSearch.text.toString())

        initTrackAdapter()
        initHistoryAdapter()
        initListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SEARCH_TEXT", binding.inputSearch.text.toString())
    }

    private fun initHistoryAdapter() {
        historyAdapter.itemClickListener = { track ->
            onTrackClickDebounce(track)
        }

        binding.searchHistoryRecycler.adapter = historyAdapter
        binding.searchHistoryRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun initTrackAdapter() {
        trackAdapter.itemClickListener = { track ->
            onTrackClickDebounce(track)
            viewModel.addTrackToHistory(track)
        }

        binding.tracklistRecycler.adapter = trackAdapter
        binding.tracklistRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun initListeners() {
        binding.inputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.changeText(s.toString())
                viewModel.searchDebounce(changedText = s?.toString() ?: "")
                searchText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        )

        binding.inputSearch.setOnFocusChangeListener { _, hasFocus ->
            viewModel.searchFocusChanged(hasFocus, binding.inputSearch.text.toString())
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
            binding.searchHistoryViewGroup.visibility = View.GONE
        }

        binding.clearSearchTextButton.setOnClickListener {
            viewModel.clearSearchText()
            trackAdapter.tracks.clear()
            trackAdapter.notifyDataSetChanged()
        }

        binding.updateSearchButton.setOnClickListener {
            binding.inputSearch.setText(searchText)
            viewModel.loadTracks(searchText)
            hideKeyboard()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.inputSearch.removeTextChangedListener(null)
    }

    private fun showPreLoading(clearSearchTextButtonVisible: Boolean) {
        if (clearSearchTextButtonVisible) {
            binding.clearSearchTextButton.visibility = View.VISIBLE
        } else {
            binding.clearSearchTextButton.visibility = View.GONE
            binding.inputSearch.setText("")
        }
        binding.progressBar.visibility = View.GONE
        binding.placeholder.visibility = View.GONE
        binding.updateSearchButton.visibility = View.GONE
    }

    private fun showLoading() {
        trackAdapter.tracks.clear()
        trackAdapter.notifyDataSetChanged()
        binding.progressBar.visibility = View.VISIBLE
        binding.placeholder.visibility = View.GONE
        binding.searchHistoryViewGroup.visibility = View.GONE
        binding.updateSearchButton.visibility = View.GONE
        hideKeyboard()
    }

    private fun showError(errorMessage: String) {
        binding.progressBar.visibility = View.GONE
        binding.tracklistRecycler.visibility = View.GONE
        binding.searchHistoryViewGroup.visibility = View.GONE
        binding.updateSearchButton.visibility = View.VISIBLE
        binding.placeholder.visibility = View.VISIBLE
        binding.placeholderImage.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.placeholderImage.setImageResource(R.drawable.something_went_wrong)
        binding.placeholderMessage.text = errorMessage
        hideKeyboard()
    }

    private fun showEmpty() {
        binding.progressBar.visibility = View.GONE
        binding.tracklistRecycler.visibility = View.GONE
        binding.searchHistoryViewGroup.visibility = View.GONE
        binding.updateSearchButton.visibility = View.GONE
        binding.placeholder.visibility = View.VISIBLE
        binding.placeholderImage.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.placeholderImage.setImageResource(R.drawable.nothing_found)
        binding.placeholderMessage.text = message
        hideKeyboard()
    }

    private fun showContent(tracks: List<TrackUi>) {
        binding.progressBar.visibility = View.GONE
        binding.tracklistRecycler.visibility = View.VISIBLE
        binding.searchHistoryViewGroup.visibility = View.GONE
        binding.placeholder.visibility = View.GONE

        historyAdapter.tracks.clear()
        historyAdapter.notifyDataSetChanged()

        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(tracks)
        trackAdapter.notifyDataSetChanged()

        hideKeyboard()
    }

    private fun showHistory(tracksHistory: List<TrackUi>, clearSearch: Boolean) {
        if (clearSearch) {
            clearSearchText()
            hideKeyboard()
        }
        binding.searchHistoryViewGroup.visibility = if (tracksHistory.isNotEmpty()) View.VISIBLE else View.GONE

        binding.clearSearchTextButton.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.tracklistRecycler.visibility = View.GONE

        trackAdapter.tracks.clear()
        trackAdapter.notifyDataSetChanged()

        historyAdapter.tracks.clear()
        historyAdapter.tracks.addAll(tracksHistory)
        historyAdapter.notifyDataSetChanged()

    }

    fun clearSearchText() {
        binding.inputSearch.setText("")
        hideKeyboard()
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.inputSearch.windowToken, 0)
    }

}