package com.practicum.playlistmaker.ui.models

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.ui.player.activity.PlayerActivity

class NavigationRouter(
    val activity: AppCompatActivity
) {

    fun goBack() {
        activity.finish()
    }

    inline fun <reified T : Activity> openActivity(context: Context) {
        val intent = Intent(activity, T::class.java)
        context.startActivity(intent)
    }

    fun openPlayer(trackId: Int) {
        val intent = Intent(activity, PlayerActivity::class.java)
        passDataToNextScreen(intent, trackId);
        activity.startActivity(intent)
    }

    private fun passDataToNextScreen(intent: Intent, trackId: Int) {
        intent.putExtra("trackId", trackId)
    }
}
