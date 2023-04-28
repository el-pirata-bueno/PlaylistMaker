package com.practicum.playlistmaker.presentation.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.presentation.models.TrackUi
import com.practicum.playlistmaker.presentation.player.PlayerActivity

class SearchRouter(
    private val activity: AppCompatActivity
) {

    fun openTrack(track: TrackUi) {
        val playerIntent = Intent(activity, PlayerActivity::class.java)
        playerIntent.putExtra("track", track)
        activity.startActivity(playerIntent)
    }

    fun goBack() {
        activity.finish()
    }


}