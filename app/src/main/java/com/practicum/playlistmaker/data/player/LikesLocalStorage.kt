package com.practicum.playlistmaker.data.player

import android.content.SharedPreferences

class LikesLocalStorage(private val sharedPreferences: SharedPreferences) {
    private companion object {
        const val LIKED_KEY = "LIKED_KEY"
    }

    fun likeTrack(trackId: Int) {
        changeLiked(trackId = trackId, remove = false)
    }

    fun unlikeTrack(trackId: Int) {
        changeLiked(trackId = trackId, remove = true)
    }

    fun getLiked(): Set<String> {
        return sharedPreferences.getStringSet(LIKED_KEY, emptySet()) ?: emptySet()
    }

    private fun changeLiked(trackId: Int, remove: Boolean) {
        val mutableSet = getLiked().toMutableSet()
        val modified =
            if (remove) mutableSet.remove(trackId.toString()) else mutableSet.add(trackId.toString())
        if (modified) sharedPreferences.edit().putStringSet(LIKED_KEY, mutableSet).apply()
    }
}
