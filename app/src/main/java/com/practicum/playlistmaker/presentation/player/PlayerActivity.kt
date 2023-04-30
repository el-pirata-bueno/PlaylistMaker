package com.practicum.playlistmaker.presentation.player

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
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.data.player.PlayerState
import com.practicum.playlistmaker.presentation.models.TrackUi
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity(), PlayerScreenView {

    companion object {
        const val PLAYTIME_UPDATE_DELAY = 250L
    }

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var presenter: PlayerPresenter

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

        val currentTrack = intent.getParcelableExtra<TrackUi>("track")

        presenter = Creator.providePlayerPresenter(
            view = this,
            trackId = currentTrack?.trackId.toString()
        )

        initView()

        if (currentTrack != null) {
            previewUrl = currentTrack.previewUrl!!
            presenter.preparePlayer(previewUrl)
            drawTrack(currentTrack)
        }

        arrowBackButton.setOnClickListener {
            presenter.arrowBackButtonClicked()
        }

        playButton.setOnClickListener {
            presenter.onPlayButtonClicked()
        }

        addToPlaylistButton.setOnClickListener {
            presenter.onAddToPlaylistButtonClicked()
        }

        addToFavouritesButton.setOnClickListener {
            presenter.onAddToFavouritesButtonClicked()
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTimer())
        presenter.onViewDestroyed()
    }

    override fun playerPrepared() {
        playButton.isEnabled = true
    }

    override fun startedTrack() {
        playButton.setImageResource(R.drawable.button_pause)
    }

    override fun pausedTrack() {
        handler.removeCallbacks(updateTimer())
        playButton.setImageResource(R.drawable.button_play)
    }

    override fun addedToPlaylist(added: Boolean) {
        addToPlaylistButton.setImageResource(
            if (added) R.drawable.button_added_to_playlist else R.drawable.button_add_to_playlist
        )
    }

    override fun addedToFavourites(added: Boolean) {
        addToFavouritesButton.setImageResource(
            if (added) R.drawable.button_added_to_favourites else R.drawable.button_add_to_favourites
        )
    }

    override fun startTimer() {
        handler.post(
            updateTimer()
        )
    }

    override fun updateTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                var state = presenter.getPlayerState()
                if (state == PlayerState.STATE_PLAYING) {
                    trackTime.text = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(presenter.getPlayerCurrentPosition())
                    handler.postDelayed(this, PLAYTIME_UPDATE_DELAY)
                }
                if (state == PlayerState.STATE_PREPARED) {
                    trackTime.text = "0:00"
                    playButton.setImageResource(R.drawable.button_play)
                    handler.removeCallbacks(updateTimer())
                }
            }
        }
    }

    override fun goBack() {
        finish()
    }

    override fun drawTrack(track: TrackUi?) {
        trackLength.text =
            SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(track!!.trackTimeMillis)

        if (track.collectionName != null) {
            trackAlbum.text = track.collectionName
        } else {
            trackAlbum.text = ""
        }

        trackName.text = track.trackName
        artistName.text = track.artistName
        trackYear.text = track.releaseDate.substring(0, 4)
        trackGenre.text = track.primaryGenreName
        artistCountry.text = track.country

        Glide.with(applicationContext)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.cover_placeholder_big)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_cover_rounded_corners)))
            .into(trackCover)
    }

    /* Удалить?
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(PLAYLIST_STATUS, addedToPlaylist)
        outState.putBoolean(FAVOURITES_STATUS, addedToFavourites)
    }
    */

    /* Удалить?
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        addedToPlaylist = savedInstanceState.getBoolean(PLAYLIST_STATUS, false)
        addedToFavourites = savedInstanceState.getBoolean(FAVOURITES_STATUS, false)
    }
    */

    /* Удалить?
    //override fun playerCompleted() {
    //    trackTime.text = "0:00"
    //    handler.removeCallbacks(updateTimer())
    //    playButton.setImageResource(R.drawable.button_play)
    //}
     */

    private fun initView() {
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
}


