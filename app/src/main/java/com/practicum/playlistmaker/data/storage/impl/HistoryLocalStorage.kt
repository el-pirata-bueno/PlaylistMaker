package com.practicum.playlistmaker.data.storage.impl

import android.content.SharedPreferences
import com.practicum.playlistmaker.data.storage.HistoryStorage

class HistoryLocalStorage(private val sharedPreferences: SharedPreferences) : HistoryStorage {

    private companion object {
        const val SEARCH_HISTORY = "SEARCH_HISTORY"
    }

    override fun getHistory(): String = sharedPreferences.getString(SEARCH_HISTORY, "") ?: ""

    override fun saveHistory(json: String) {
        sharedPreferences.edit().putString(SEARCH_HISTORY, json).apply()
    }

    override fun clearHistory() {
        sharedPreferences.edit().remove(SEARCH_HISTORY).apply()
    }
}
