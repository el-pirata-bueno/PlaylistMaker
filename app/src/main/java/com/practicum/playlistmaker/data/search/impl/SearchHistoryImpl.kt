package com.practicum.playlistmaker.data.search.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.search.SearchHistory
import com.practicum.playlistmaker.data.storage.impl.HistoryLocalStorage
import com.practicum.playlistmaker.domain.model.Track

class SearchHistoryImpl(
    private val localStorage: HistoryLocalStorage,
    private val gson: Gson
) : SearchHistory {

    companion object {
        const val MAX_HISTORY_SIZE = 10
    }

    override suspend fun getHistory(): List<Track> {
        val json = localStorage.getHistory()
        val data: List<Track> = gson.fromJson(json, object : TypeToken<ArrayList<Track?>?>() {}.type) ?: emptyList()

        return data
    }

    override fun saveHistory(tracks: List<Track>) {
        val json = gson.toJson(tracks)
        localStorage.saveHistory(json)
    }

    override fun clearHistory() {
        localStorage.clearHistory()
    }

    override suspend fun addTrackToHistory(track: Track) {
        val history = getHistory().toMutableList()
        for (i in history.indices) {
            if (track.trackId == history[i].trackId) {
                history.removeAt(i)
                break
            }
        }

        if (history.size < MAX_HISTORY_SIZE) {
            history.add(0, track)
        } else {
            history.removeAt(MAX_HISTORY_SIZE - 1)
            history.add(0, track)
        }

        saveHistory(history)
    }
}