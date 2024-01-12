package com.practicum.playlistmaker.data.search

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.data.db.LikedTracksDatabase
import com.practicum.playlistmaker.data.storage.impl.HistoryLocalStorage
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.search.SearchHistory

class SearchHistoryImpl(
    private val localStorage: HistoryLocalStorage,
    private val appDatabase: LikedTracksDatabase,
    private val trackDbConvertor: TrackDbConvertor
    ) : SearchHistory {

    companion object {
        const val MAX_HISTORY_SIZE = 10
    }

    override fun getHistory(): List<Track> {
        // Перепроверить работу при возврате назад к истории"
        val json = localStorage.getHistory()
        val data: List<Track> = Gson().fromJson(json, object : TypeToken<ArrayList<Track?>?>() {}.type) ?: emptyList()
        val likedTracks = appDatabase.trackDao().getTrackIds()
        for (track in data) {
            for (likedTrack in likedTracks) {
                if (track.trackId == likedTrack) {
                    track.isFavorite = true
                    break
                }
            }
        }
        return data
    }

    override fun saveHistory(tracks: List<Track>) {
        val json = Gson().toJson(tracks)
        localStorage.saveHistory(json)
    }

    override fun clearHistory() {
        localStorage.clearHistory()
    }

    override fun addTrackToHistory(track: Track) {
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