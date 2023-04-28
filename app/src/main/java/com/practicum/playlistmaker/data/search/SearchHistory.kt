package com.practicum.playlistmaker.data.search

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.TRACKLIST_HISTORY
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.SearchHistoryInterface


class SearchHistory(var sharedPreferences: SharedPreferences) : SearchHistoryInterface {

    companion object {
        const val MAX_HISTORY_SIZE = 10
    }

    // чтение истории
    override fun read(): ArrayList<Track> {
        val json = sharedPreferences.getString(TRACKLIST_HISTORY, null) ?: return ArrayList()
        return Gson().fromJson(json, object : TypeToken<ArrayList<Track?>?>() {}.type)
    }

    // запись в историю
    override fun write(tracks: List<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit() {
            putString(TRACKLIST_HISTORY, json)
        }
    }

    // очистка истории
    override fun delete() {
        sharedPreferences.edit() {
            clear()
        }
    }

    // добавление трека
    override fun addTrack(track: Track) {
        var history = read()
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

        write(history)
    }
}