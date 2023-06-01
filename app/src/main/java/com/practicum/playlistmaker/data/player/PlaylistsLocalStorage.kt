package com.practicum.playlistmaker.data.player

import android.content.SharedPreferences

class PlaylistsLocalStorage(private val sharedPreferences: SharedPreferences) {
    private companion object {
        const val PLAYLISTS_KEY = "PLAYLISTS_KEY"
    }

    fun addTrackToPlaylist(trackId: Int) {
        changePlaylists(trackId = trackId, remove = false)
    }

    fun removeTrackFromPlaylist(trackId: Int) {
        changePlaylists(trackId = trackId, remove = true)
    }

    fun getPlaylists(): Set<String> {
        return sharedPreferences.getStringSet(PLAYLISTS_KEY, emptySet()) ?: emptySet()
    }

    private fun changePlaylists(trackId: Int, remove: Boolean) {
        val mutableSet = getPlaylists().toMutableSet()
        val modified =
            if (remove) mutableSet.remove(trackId.toString()) else mutableSet.add(trackId.toString())
        if (modified) sharedPreferences.edit().putStringSet(PLAYLISTS_KEY, mutableSet).apply()
    }
}
