package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ViewUtils.hideKeyboard
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
    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter()

    var userText: String = ""
    lateinit var placeholderMessage: TextView
    lateinit var placeholderImage: ImageView
    lateinit var updateButton: Button
    lateinit var searchText: String
    lateinit var inputEditText: EditText
    lateinit var clearButton: ImageView
    lateinit var arrowBackButton: Button
    lateinit var trackList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initVars()

        adapter.tracks = tracks
        trackList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trackList.adapter = adapter

        arrowBackButtonListener()
        clearButtonListener()
        updateButtonListener()
        inputEditTextListener()

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                userText = inputEditText.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        inputEditText.addTextChangedListener(simpleTextWatcher)

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
                            adapter.notifyDataSetChanged()
                        }
                        if (tracks.isEmpty()) {
                            showMessage(getString(R.string.nothing_found))
                        } else {
                            showMessage("")
                        }
                    } else {
                        showMessage(getString(R.string.something_went_wrong))
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    showMessage(getString(R.string.something_went_wrong))
                }
            }
            )
        }
    }

    private fun showMessage(text: String) {
        if (text.isNotEmpty()) {
            if (text.equals(getString(R.string.nothing_found))) {
                placeholderMessage.visibility = View.VISIBLE
                placeholderImage.setImageResource(R.drawable.nothing_found)
                placeholderImage.visibility = View.VISIBLE
                updateButton.visibility = View.GONE
                tracks.clear()
                adapter.notifyDataSetChanged()
                placeholderMessage.text = text
            }
            if (text.equals(getString(R.string.something_went_wrong))) {
                placeholderMessage.visibility = View.VISIBLE
                placeholderImage.setImageResource(R.drawable.something_went_wrong)
                placeholderImage.visibility = View.VISIBLE
                updateButton.visibility = View.VISIBLE
                tracks.clear()
                adapter.notifyDataSetChanged()
                placeholderMessage.text = text
            }
        } else {
            placeholderMessage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            updateButton.visibility = View.GONE
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
            adapter.notifyDataSetChanged()
            placeholderMessage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            updateButton.visibility = View.GONE
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
}
