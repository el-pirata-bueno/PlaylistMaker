package com.practicum.playlistmaker.data.storage

import android.content.SharedPreferences

class HistoryLocalStorage(private val sharedPreferences: SharedPreferences) {
    private companion object {
        const val SEARCH_HISTORY = "SEARCH_HISTORY"
    }

    fun getHistory(): String = sharedPreferences.getString(SEARCH_HISTORY, "") ?: ""

    fun saveHistory(json: String) {
        sharedPreferences.edit().putString(SEARCH_HISTORY, json).apply()
    }

    fun clearHistory() {
        sharedPreferences.edit().remove(SEARCH_HISTORY).apply()
    }
}
