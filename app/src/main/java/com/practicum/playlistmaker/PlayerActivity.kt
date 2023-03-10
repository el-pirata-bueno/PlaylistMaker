package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val currentTrack = intent.getParcelableExtra<Track>("track")

        val trackName: TextView = findViewById(R.id.track_name)
        val artistName: TextView = findViewById(R.id.artist_name)
        val trackLength: TextView = findViewById(R.id.track_length)
        val trackAlbum: TextView = findViewById(R.id.track_album)
        val trackYear: TextView = findViewById(R.id.track_year)
        val trackGenre: TextView = findViewById(R.id.track_genre)
        val artistCountry: TextView = findViewById(R.id.artist_country)
        val trackCover: ImageView = findViewById(R.id.track_cover_big)
        val arrowBackButton = findViewById<Button>(R.id.arrow_back_player)
        val playButton: ImageButton = findViewById(R.id.button_play)
        val pauseButton: ImageButton = findViewById(R.id.button_pause)
        val addToPlaylistButton: ImageButton = findViewById(R.id.button_add_to_playlist)
        val addedToPlaylistButton: ImageButton = findViewById(R.id.button_added_to_playlist)
        val addToFavouritesButton: ImageButton = findViewById(R.id.button_add_to_favourites)
        val addedToFavouritesButton: ImageButton = findViewById(R.id.button_added_to_favourites)

        arrowBackButton.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

            trackLength.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTrack!!.trackTimeMillis)
            if (currentTrack.collectionName != null) {
                trackAlbum.text = currentTrack.collectionName
            }
            trackName.text = currentTrack.trackName
            artistName.text = currentTrack.artistName
            trackYear.text = getYear(currentTrack)
            trackGenre.text = currentTrack.primaryGenreName
            artistCountry.text = currentTrack.country

            Glide.with(applicationContext)
                .load(getCoverArtwork(currentTrack))
                .placeholder(R.drawable.cover_placeholder_big)
                .centerCrop()
                .transform(RoundedCorners(8))
                .into(trackCover)

        playButton.setOnClickListener {
            playButton.visibility = View.GONE
            pauseButton.visibility = View.VISIBLE
        }

        pauseButton.setOnClickListener {
            pauseButton.visibility = View.GONE
            playButton.visibility = View.VISIBLE
        }

        addToPlaylistButton.setOnClickListener {
            addToPlaylistButton.visibility = View.GONE
            addedToPlaylistButton.visibility = View.VISIBLE
        }

        addedToPlaylistButton.setOnClickListener {
            addedToPlaylistButton.visibility = View.GONE
            addToPlaylistButton.visibility = View.VISIBLE
        }

        addToFavouritesButton.setOnClickListener {
            addToFavouritesButton.visibility = View.GONE
            addedToFavouritesButton.visibility = View.VISIBLE
        }

        addedToFavouritesButton.setOnClickListener {
            addedToFavouritesButton.visibility = View.GONE
            addToFavouritesButton.visibility = View.VISIBLE
        }
    }

    fun getCoverArtwork(currentTrack: Track): String? = currentTrack.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

    fun getYear(currentTrack: Track): String? = currentTrack.releaseDate.substring(0,4)

}
