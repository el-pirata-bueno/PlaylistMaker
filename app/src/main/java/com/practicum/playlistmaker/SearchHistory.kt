package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory (var sharedPreferences: SharedPreferences) {

    // чтение истории
    fun read(): Array<Track> {
        val json = sharedPreferences.getString(TRACKLIST_HISTORY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    // запись в историю
    fun write(tracks: ArrayList<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(TRACKLIST_HISTORY, json)
            .apply()
    }

    // очистка истории
    fun delete() {
        sharedPreferences.edit()
            .clear()
            .apply()
    }

}