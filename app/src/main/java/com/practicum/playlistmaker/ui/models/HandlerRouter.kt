package com.practicum.playlistmaker.ui.models

import android.os.Handler
import android.os.Looper

class HandlerRouter(looper: Looper) {

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val PLAYTIME_UPDATE_DELAY = 250L
    }

    val SEARCH_DEBOUNCE_DELAY = 2000L

    private val handler = Handler(looper)
    private var isClickAllowed = true

    fun startPlaying(r: Runnable) {
        handler.postDelayed(r, PLAYTIME_UPDATE_DELAY)
    }

    fun stopRunnable(token: Any?) {
        handler.removeCallbacksAndMessages(token)
    }

    fun postAtTime(r: Runnable, token: Any, time: Long) {
        handler.postAtTime(r, token, time)
    }
}