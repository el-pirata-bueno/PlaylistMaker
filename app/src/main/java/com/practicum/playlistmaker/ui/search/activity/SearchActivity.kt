package com.practicum.playlistmaker.ui.search.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.ui.models.NavigationRouter
import com.practicum.playlistmaker.ui.models.SearchState
import com.practicum.playlistmaker.ui.models.TrackUi
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var viewModel: SearchViewModel
    private lateinit var router: NavigationRouter
    private lateinit var binding: ActivitySearchBinding

    private val trackAdapter = TrackAdapter()
    private var historyAdapter = TrackAdapter()
    private lateinit var searchText: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val errorText: String = getString(R.string.something_went_wrong)
        val messageText: String = getString(R.string.nothing_found)

        router = NavigationRouter(this)

        viewModel = ViewModelProvider(this, SearchViewModel.getViewModelFactory(errorText, messageText))[SearchViewModel::class.java]

        viewModel.getSearchStateLiveData().observe(this) { screenState ->
            when (screenState) {
                is SearchState.Content -> showContent(screenState.tracks)
                is SearchState.History -> showHistory(screenState.historyTracks, screenState.clearSearchState)
                is SearchState.Empty -> showEmpty(screenState.message)
                is SearchState.Error -> showError(screenState.errorMessage)
                is SearchState.Loading -> showLoading()
                is SearchState.PreLoading -> {
                    binding.clearSearchTextButton.visibility = if (screenState.buttonVisible) View.VISIBLE else View.GONE
                    showPreLoading()
                }
                else -> {}
            }
        }

        viewModel.getClearSearchLiveData().observe(this) {
            if (it) {
                clearSearchText()
                hideKeyboard()
            }
        }

        viewModel.getClearSearchTextButtonLiveData().observe(this) { isVisible ->
            if (isVisible) {
                binding.clearSearchTextButton.visibility = View.VISIBLE
            }
            else {
                binding.clearSearchTextButton.visibility = View.GONE
                binding.inputSearch.setText("")
            }
        }

        initTrackAdapter()
        initHistoryAdapter()
        initListeners()

        /* Тост на перспективу
        viewModel.observeToastState().observe(this) { toast ->
            showToast(toast)
        }
         */
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
            router.openPlayer(track)
        }

        binding.searchHistoryRecycler.adapter = historyAdapter
        binding.searchHistoryRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun initTrackAdapter() {
        trackAdapter.itemClickListener = { track ->
            router.openPlayer(track)
            viewModel.addTrackToHistory(track)
        }

        binding.tracklistRecycler.adapter = trackAdapter
        binding.tracklistRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun initListeners() {
        binding.inputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.changeText(s.toString())

                if (binding.inputSearch.text.toString() != "") {
                    viewModel.searchDebounce(changedText = s?.toString() ?: "")
                    searchText = s.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) { }
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

    private fun showPreLoading() {
        //if (screenState.isTextEmpty) { binding.inputSearch.setText("") }
        binding.progressBar.visibility = View.GONE
        binding.placeholder.visibility = View.GONE
        binding.searchHistoryViewGroup.visibility = View.VISIBLE
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
        binding.placeholderMessage.text = getString(R.string.something_went_wrong)
        hideKeyboard()
    }

    private fun showEmpty(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.tracklistRecycler.visibility = View.GONE
        binding.searchHistoryViewGroup.visibility = View.GONE
        binding.updateSearchButton.visibility = View.GONE
        binding.placeholder.visibility = View.VISIBLE
        binding.placeholderImage.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.placeholderImage.setImageResource(R.drawable.nothing_found)
        binding.placeholderMessage.text = getString(R.string.nothing_found)
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

        binding.searchHistoryViewGroup.visibility = View.VISIBLE
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

    /* Тост на будущее
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    */
}