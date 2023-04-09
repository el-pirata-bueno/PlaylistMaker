package com.practicum.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    companion object {
        //const val PLAY_STATUS = "PLAY_STATUS"
        const val PLAYLIST_STATUS = "PLAYLIST_STATUS"
        const val FAVOURITES_STATUS = "FAVOURITES_STATUS"
        const val PLAYTIME_UPDATE_DELAY = 250L
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    //var playOn = false
    var addedToPlaylist = false
    var addedToFavourites = false

    private var mediaPlayer = MediaPlayer()
        private var playerState = STATE_DEFAULT

    private val handler = Handler(Looper.getMainLooper())

    lateinit var trackName: TextView
    lateinit var artistName: TextView
    lateinit var trackLength: TextView
    lateinit var trackAlbum: TextView
    lateinit var trackYear: TextView
    lateinit var trackGenre: TextView
    lateinit var artistCountry: TextView
    lateinit var trackTime: TextView
    lateinit var trackCover: ImageView
    lateinit var arrowBackButton: Button
    lateinit var playButton: ImageButton
    lateinit var addToPlaylistButton: ImageButton
    lateinit var addToFavouritesButton: ImageButton
    lateinit var previewUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val currentTrack = intent.getParcelableExtra<Track>("track")

        initVars()

        arrowBackButton.setOnClickListener {
            finish()
        }

        if (currentTrack != null) {
            previewUrl = currentTrack.previewUrl

            preparePlayer()

            trackLength.text =
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(currentTrack!!.trackTimeMillis)

            if (currentTrack.collectionName != null) {
                trackAlbum.text = currentTrack.collectionName
            }
            else {
                trackAlbum.text = ""
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
        }

        playButton.setOnClickListener {
            playbackControl()
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

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(updateTimer())
    }

    fun getCoverArtwork(currentTrack: Track): String? =
        currentTrack.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    fun getYear(currentTrack: Track): String? = currentTrack.releaseDate.substring(0, 4)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //outState.putBoolean(PLAY_STATUS, playOn)
        outState.putBoolean(PLAYLIST_STATUS, addedToPlaylist)
        outState.putBoolean(FAVOURITES_STATUS, addedToFavourites)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        //playOn = savedInstanceState.getBoolean(PLAY_STATUS, false)
        addedToPlaylist = savedInstanceState.getBoolean(PLAYLIST_STATUS, false)
        addedToFavourites = savedInstanceState.getBoolean(FAVOURITES_STATUS, false)
    }

    private fun initVars() {
        trackName = findViewById(R.id.track_name)
        artistName = findViewById(R.id.artist_name)
        trackLength = findViewById(R.id.track_length)
        trackAlbum = findViewById(R.id.track_album)
        trackYear = findViewById(R.id.track_year)
        trackGenre = findViewById(R.id.track_genre)
        artistCountry = findViewById(R.id.artist_country)
        trackTime = findViewById(R.id.current_track_time)
        trackCover = findViewById(R.id.track_cover_big)
        arrowBackButton = findViewById(R.id.arrow_back_player)
        playButton = findViewById(R.id.button_play)
        addToPlaylistButton = findViewById(R.id.button_add_to_playlist)
        addToFavouritesButton = findViewById(R.id.button_add_to_favourites)
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            handler.removeCallbacks(updateTimer())
            playButton.setImageResource(R.drawable.button_play)
            trackTime.text = "0:00"
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.button_pause)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        handler.removeCallbacks(updateTimer())
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.button_play)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()

            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
                startTimer()
            }
        }
    }

    private fun startTimer() {
        handler.post(
            updateTimer()
        )
    }

    private fun updateTimer (): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    trackTime.text = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
                    handler?.postDelayed(this, PLAYTIME_UPDATE_DELAY)
                }

                }
            }
        }
    }


