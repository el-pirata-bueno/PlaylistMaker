package com.practicum.playlistmaker.presentation.search

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.SEARCH_HISTORY
import com.practicum.playlistmaker.TrackAdapter
import com.practicum.playlistmaker.data.search.SearchHistory
import com.practicum.playlistmaker.data.search.SearchRepository
import com.practicum.playlistmaker.domain.api.ITunesApi
import com.practicum.playlistmaker.domain.impl.SearchInteractor
import com.practicum.playlistmaker.presentation.models.TrackUi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), SearchScreenView {

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val repository = SearchRepository(retrofit.create(ITunesApi::class.java))
    private val searchRunnable = Runnable { loadTracks() }
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var presenter: SearchPresenter
    private var isClickAllowed = true

    val trackAdapter = TrackAdapter()
    var historyAdapter = TrackAdapter()

    lateinit var placeholderMessage: TextView
    lateinit var placeholderImage: ImageView
    lateinit var updateButton: Button
    lateinit var inputEditText: EditText
    lateinit var clearButton: ImageView
    lateinit var arrowBackButton: Button
    lateinit var trackListRecycler: RecyclerView
    lateinit var searchHistoryTitle: TextView
    lateinit var deleteHistoryButton: Button
    lateinit var searchHistoryRecycler: RecyclerView
    lateinit var searchHistoryViewGroup: FrameLayout
    lateinit var placeholder: FrameLayout
    lateinit var progressBar: ProgressBar
    lateinit var sharedPrefsSearchHistory: SharedPreferences
    private lateinit var searchHistoryList: SearchHistory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initView()
        initHistory()
        initTrackAdapter()
        initHistoryAdapter()

        presenter = SearchPresenter(
            view = this,
            interactor = SearchInteractor(
                searchHistoryList, repository
            ),
            router = SearchRouter(this)
        )

        arrowBackButton.setOnClickListener {
            presenter.arrowBackButtonClicked()
        }

        deleteHistoryButton.setOnClickListener {
            presenter.onHistoryDeleteClicked()
            searchHistoryViewGroup.visibility = View.GONE
        }

        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            presenter.searchFocusChanged(hasFocus, inputEditText.text.toString())
        }

        clearButton.setOnClickListener {
            presenter.searchTextClearClicked()
            trackAdapter.tracks.clear()
            trackAdapter.notifyDataSetChanged()
        }

        updateButton.setOnClickListener {
            loadTracks()
            hideKeyboard()
        }

        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (inputEditText.text.toString() != "") {
                    searchDebounce()
                    searchHistoryViewGroup.visibility = View.GONE
                }
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        })
    }

    override fun showHistory(tracksHistory: List<TrackUi>) {
        searchHistoryViewGroup.visibility = View.VISIBLE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.GONE
        trackListRecycler.visibility = View.GONE

        trackAdapter.tracks.clear()
        trackAdapter.notifyDataSetChanged()

        historyAdapter.tracks.clear()
        historyAdapter.tracks.addAll(tracksHistory)
        historyAdapter.notifyDataSetChanged()
    }

    override fun showEmptyResult() {
        progressBar.visibility = View.GONE
        trackListRecycler.visibility = View.GONE
        searchHistoryViewGroup.visibility = View.GONE
        updateButton.visibility = View.GONE
        placeholder.visibility = View.VISIBLE
        placeholderImage.visibility = View.VISIBLE
        placeholderMessage.visibility = View.VISIBLE
        placeholderImage.setImageResource(R.drawable.nothing_found)
        placeholderMessage.text = getString(R.string.nothing_found)
    }

    override fun showTracks(tracks: List<TrackUi>) {
        progressBar.visibility = View.GONE
        trackListRecycler.visibility = View.VISIBLE
        searchHistoryViewGroup.visibility = View.GONE
        placeholder.visibility = View.GONE

        historyAdapter.tracks.clear()
        historyAdapter.notifyDataSetChanged()

        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(tracks)
        trackAdapter.notifyDataSetChanged()
    }

    override fun showSearchError() {
        progressBar.visibility = View.GONE
        trackListRecycler.visibility = View.GONE
        searchHistoryViewGroup.visibility = View.GONE
        updateButton.visibility = View.VISIBLE
        placeholder.visibility = View.VISIBLE
        placeholderImage.visibility = View.VISIBLE
        placeholderMessage.visibility = View.VISIBLE
        placeholderImage.setImageResource(R.drawable.something_went_wrong)
        placeholderMessage.text = getString(R.string.something_went_wrong)
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    override fun clearSearchText() {
        inputEditText.setText("")
        progressBar.visibility = View.GONE
        placeholder.visibility = View.GONE
        searchHistoryViewGroup.visibility = View.GONE
    }

    override fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
    }

    override fun showLoading() {
        trackAdapter.tracks.clear()
        trackAdapter.notifyDataSetChanged()
        progressBar.visibility = View.VISIBLE
    }

    private fun loadTracks() {
        progressBar.visibility = View.VISIBLE
        placeholder.visibility = View.GONE
        presenter.search(inputEditText.text.toString())
    }

    private fun initView() {
        placeholderMessage = findViewById(R.id.placeholderMessage)
        placeholderImage = findViewById(R.id.placeholderImage)
        updateButton = findViewById(R.id.updateButton)
        inputEditText = findViewById(R.id.inputSearch)
        clearButton = findViewById(R.id.clearIcon)
        arrowBackButton = findViewById(R.id.arrow_back_search)
        trackListRecycler = findViewById(R.id.trackList)
        searchHistoryTitle = findViewById(R.id.searchHistoryTitle)
        deleteHistoryButton = findViewById(R.id.deleteHistory)
        searchHistoryRecycler = findViewById(R.id.searchHistory)
        searchHistoryViewGroup = findViewById(R.id.searchHistoryViewGroup)
        placeholder = findViewById(R.id.placeholder)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun initHistory() {
        sharedPrefsSearchHistory = getSharedPreferences(SEARCH_HISTORY, MODE_PRIVATE)
        searchHistoryList = SearchHistory(sharedPrefsSearchHistory)
    }

    private fun initHistoryAdapter() {
        historyAdapter.itemClickListener = { track ->
            presenter.onTrackClicked(track)
        }

        searchHistoryRecycler.adapter = historyAdapter
        searchHistoryRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun initTrackAdapter() {
        trackAdapter.itemClickListener = { track ->
            presenter.onTrackClicked(track)
        }

        trackListRecycler.adapter = trackAdapter
        trackListRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun clickDebounce(): Boolean {
        val current: Boolean = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

}
