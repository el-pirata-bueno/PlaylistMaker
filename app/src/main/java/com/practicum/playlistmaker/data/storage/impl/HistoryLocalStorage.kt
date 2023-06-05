package com.practicum.playlistmaker.data.storage.impl

import android.content.SharedPreferences
import com.practicum.playlistmaker.data.storage.HistoryStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class HistoryLocalStorage(
    //private val sharedPreferences: SharedPreferences
    ) : HistoryStorage, KoinComponent {

    private companion object {
        const val SEARCH_HISTORY = "SEARCH_HISTORY"
    }
    val sharedPreferences: SharedPreferences by inject(qualifier = named("historyPrefs"))

    override fun getHistory(): String = sharedPreferences.getString(SEARCH_HISTORY, "") ?: ""

    override fun saveHistory(json: String) {
        sharedPreferences.edit().putString(SEARCH_HISTORY, json).apply()
    }

    override fun clearHistory() {
        sharedPreferences.edit().remove(SEARCH_HISTORY).apply()
    }
}
