package com.practicum.playlistmaker.ui.search.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.ui.models.NavigationRouter
import com.practicum.playlistmaker.ui.models.SearchState
import com.practicum.playlistmaker.ui.models.TrackUi
import com.practicum.playlistmaker.ui.search.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {
    private val viewModel: SearchViewModel by viewModel()
    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private val router: NavigationRouter by lazy { NavigationRouter(this) }

    private val trackAdapter = TrackAdapter()
    private var historyAdapter = TrackAdapter()
    private lateinit var searchText: String
    private var message = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        message = getString(R.string.nothing_found)

        viewModel.getSearchStateLiveData().observe(this) { screenState ->
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.inputSearch.setText(savedInstanceState.getString("SEARCH_TEXT", ""))
    }

    private fun initHistoryAdapter() {
        historyAdapter.itemClickListener = { track ->
            router.openPlayer(track.trackId)
        }

        binding.searchHistoryRecycler.adapter = historyAdapter
        binding.searchHistoryRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun initTrackAdapter() {
        trackAdapter.itemClickListener = { track ->
            router.openPlayer(track.trackId)
            viewModel.addTrackToHistory(track)
        }

        binding.tracklistRecycler.adapter = trackAdapter
        binding.tracklistRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun initListeners() {
        binding.inputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.changeText(s.toString())
                viewModel.searchDebounce(changedText = s?.toString() ?: "")
                searchText = s.toString()

                if (binding.inputSearch.text.toString() != "") {

                //    viewModel.searchDebounce(changedText = s?.toString() ?: "")
                //    searchText = s.toString()
                }
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

        binding.arrowBackButton.setOnClickListener {
            router.goBack()
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
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.inputSearch.windowToken, 0)
    }
}
