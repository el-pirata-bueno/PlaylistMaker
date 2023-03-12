package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

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
        val addToPlaylistButton: ImageButton = findViewById(R.id.button_add_to_playlist)
        val addToFavouritesButton: ImageButton = findViewById(R.id.button_add_to_favourites)
        var playOn = false
        var addedToPlaylist = false
        var addedToFavourites = false

        arrowBackButton.setOnClickListener {
            finish()
        }

        trackLength.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTrack!!.trackTimeMillis)
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
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_cover_rounded_corners)))
            .into(trackCover)

        playButton.setOnClickListener {
            if (playOn) {
                playOn = false
                playButton.setImageResource(R.drawable.button_play)
            }
            else {
                playOn = true
                playButton.setImageResource(R.drawable.button_pause)
            }
        }

        addToPlaylistButton.setOnClickListener {
            if (addedToPlaylist) {
                addedToPlaylist = false
                addToPlaylistButton.setImageResource(R.drawable.button_add_to_playlist)
            }
            else {
                addedToPlaylist = true
                addToPlaylistButton.setImageResource(R.drawable.button_added_to_playlist)
            }
        }

        addToFavouritesButton.setOnClickListener {
            if (addedToFavourites) {
                addedToFavourites = false
                addToFavouritesButton.setImageResource(R.drawable.button_add_to_favourites)
            }
            else {
                addedToFavourites = true
                addToFavouritesButton.setImageResource(R.drawable.button_added_to_favourites)
            }
        }

    }

    fun getCoverArtwork(currentTrack: Track): String? =
        currentTrack.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    fun getYear(currentTrack: Track): String? = currentTrack.releaseDate.substring(0, 4)

}
