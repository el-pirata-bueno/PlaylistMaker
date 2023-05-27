package com.practicum.playlistmaker.data.search

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.SearchHistory

class TracksSearchHistory(private val localStorage: HistoryLocalStorage) : SearchHistory {

    companion object {
        const val MAX_HISTORY_SIZE = 10
    }

    // чтение истории
    override fun getHistory(): List<Track> {
        val json = localStorage.getHistory()
        return Gson().fromJson(json, object : TypeToken<ArrayList<Track?>?>() {}.type) ?: emptyList()
    }

    // запись в историю
    override fun saveHistory(tracks: List<Track>) {
        val json = Gson().toJson(tracks)
        localStorage.saveHistory(json)
    }

    // очистка истории
    override fun clearHistory() {
        localStorage.clearHistory()
    }

    // добавление трека
    override fun addTrackToHistory(track: Track) {
        var history = getHistory().toMutableList()
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