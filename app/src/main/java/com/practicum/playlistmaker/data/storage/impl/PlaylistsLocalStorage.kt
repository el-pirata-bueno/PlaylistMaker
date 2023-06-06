package com.practicum.playlistmaker.data.storage.impl

import android.content.SharedPreferences
import com.practicum.playlistmaker.data.storage.PlaylistsStorage

class PlaylistsLocalStorage(private val sharedPreferences: SharedPreferences): PlaylistsStorage {

    private companion object {
        const val PLAYLISTS_KEY = "PLAYLISTS_KEY"
    }

    override fun addTrackToPlaylist(trackId: Int) {
        changePlaylists(trackId = trackId, remove = false)
    }

    override fun removeTrackFromPlaylist(trackId: Int) {
        changePlaylists(trackId = trackId, remove = true)
    }

    override fun getPlaylists(): Set<String> {
        return sharedPreferences.getStringSet(PLAYLISTS_KEY, emptySet()) ?: emptySet()
    }

    private fun changePlaylists(trackId: Int, remove: Boolean) {
        val mutableSet = getPlaylists().toMutableSet()
        val modified =
            if (remove) mutableSet.remove(trackId.toString()) else mutableSet.add(trackId.toString())
        if (modified) sharedPreferences.edit().putStringSet(PLAYLISTS_KEY, mutableSet).apply()
    }
}