package com.practicum.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.practicum.playlistmaker.SearchActivity.Companion.SEARCH_TEXT
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    val tracks = ArrayList<Track>()
    val trackAdapter = TrackAdapter()
    var tracksHistory = ArrayList<Track>()
    val historyAdapter = TrackAdapter()

    var userText: String = ""
    lateinit var placeholderMessage: TextView
    lateinit var placeholderImage: ImageView
    lateinit var updateButton: Button
    lateinit var searchText: String
    lateinit var inputEditText: EditText
    lateinit var clearButton: ImageView
    lateinit var arrowBackButton: Button
    lateinit var trackList: RecyclerView
    lateinit var searchHistoryTitle: TextView
    lateinit var deleteHistoryButton: Button
    lateinit var searchHistory: RecyclerView
    lateinit var searchHistoryViewGroup: FrameLayout
    lateinit var placeholder: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initVars()

        val sharedPrefsSearchHistory: SharedPreferences = getSharedPreferences(SEARCH_HISTORY, MODE_PRIVATE)
        val searchHistoryList = SearchHistory(sharedPrefsSearchHistory)

        trackAdapter.tracks = tracks
        trackList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trackList.adapter = trackAdapter

        historyAdapter.tracks = tracksHistory
        searchHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchHistory.adapter = historyAdapter

        arrowBackButtonListener()
        clearButtonListener()
        updateButtonListener()
        inputEditTextListener()

        deleteHistoryButton.setOnClickListener {
            tracksHistory.clear()
            searchHistoryList.delete()
            historyAdapter.notifyDataSetChanged()
            searchHistoryViewGroup.visibility = View.GONE
        }

        trackAdapter.itemClickListener = {position, track ->
            for (i in 0 until tracksHistory.size) {
                if (track.trackId == tracksHistory[i].trackId) {
                    tracksHistory.removeAt(i)
                    break
                }

            }

            if (tracksHistory.size < 10) {
                tracksHistory.add(0, track)
            }
            else {
                tracksHistory.removeAt(9)
                tracksHistory.add(0, track)
            }

            searchHistoryList.write(tracksHistory)
            historyAdapter.notifyItemInserted(position)
            Toast.makeText(this, "Трек ${track.artistName} - ${track.trackName} добавлен в историю поиска", Toast.LENGTH_SHORT).show()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                userText = inputEditText.text.toString()
                searchHistoryViewGroup.visibility = if (inputEditText.hasFocus() && tracksHistory.isNotEmpty() && s?.isEmpty() == true) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        inputEditText.addTextChangedListener(simpleTextWatcher)

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            tracksHistory.addAll(searchHistoryList.read())
            historyAdapter.notifyDataSetChanged()
            searchHistoryViewGroup.visibility = if (inputEditText.hasFocus() && tracksHistory.isNotEmpty() && inputEditText.text.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun search(text: String) {
        if (text.isNotEmpty()) {
            iTunesService.searchTracks(text).enqueue(object :
                Callback<TracksResponse> {
                override fun onResponse(call: Call<TracksResponse>,
                                        response: Response<TracksResponse>) {

                if (response.code() == 200) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            trackAdapter.notifyDataSetChanged()
                        }
                        if (tracks.isEmpty()) {
                            showMessage(SearchStatus.EMPTY_SEARCH)
                        } else {
                            showMessage(SearchStatus.SUCCESS)
                        }
                    } else {
                        showMessage(SearchStatus.CONNECTION_ERROR)
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    showMessage(SearchStatus.CONNECTION_ERROR)
                }
            }
            )
        }
    }

    private fun showMessage(search: SearchStatus) {
        when (search) {
            SearchStatus.SUCCESS -> {
                placeholder.visibility = View.GONE
            }
            SearchStatus.CONNECTION_ERROR -> {
                placeholder.visibility = View.VISIBLE
                placeholderMessage.visibility = View.VISIBLE
                placeholderImage.setImageResource(R.drawable.something_went_wrong)
                placeholderImage.visibility = View.VISIBLE
                updateButton.visibility = View.VISIBLE
                tracks.clear()
                trackAdapter.notifyDataSetChanged()
                placeholderMessage.text = getString(R.string.something_went_wrong)
            }
            SearchStatus.EMPTY_SEARCH -> {
                placeholder.visibility = View.VISIBLE
                placeholderMessage.visibility = View.VISIBLE
                placeholderImage.setImageResource(R.drawable.nothing_found)
                placeholderImage.visibility = View.VISIBLE
                updateButton.visibility = View.GONE
                tracks.clear()
                trackAdapter.notifyDataSetChanged()
                placeholderMessage.text = getString(R.string.nothing_found)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT,userText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        userText = savedInstanceState.getString(SEARCH_TEXT,"")
        val inputEditText = findViewById<EditText>(R.id.inputSearch)
        inputEditText.setText(userText)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun initVars() {
        placeholderMessage = findViewById(R.id.placeholderMessage)
        placeholderImage = findViewById(R.id.placeholderImage)
        updateButton = findViewById(R.id.updateButton)
        inputEditText = findViewById(R.id.inputSearch)
        clearButton = findViewById(R.id.clearIcon)
        arrowBackButton = findViewById(R.id.arrow_back)
        trackList = findViewById(R.id.trackList)
        searchHistoryTitle = findViewById(R.id.searchHistoryTitle)
        deleteHistoryButton = findViewById(R.id.deleteHistory)
        searchHistory = findViewById(R.id.searchHistory)
        searchHistoryViewGroup = findViewById(R.id.searchHistoryViewGroup)
        placeholder = findViewById(R.id.placeholder)
    }

    private fun arrowBackButtonListener() {
        arrowBackButton.setOnClickListener {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }
    }

    private fun clearButtonListener() {
        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard(currentFocus ?: View(this))
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            placeholder.visibility = View.GONE
        }
    }

    private fun updateButtonListener() {
        updateButton.setOnClickListener {
            search(searchText)
        }
    }

    private fun inputEditTextListener() {
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchText = inputEditText.text.toString()
                search(searchText)
            }
            false
        }
    }

    enum class SearchStatus { SUCCESS, CONNECTION_ERROR, EMPTY_SEARCH }


    }
