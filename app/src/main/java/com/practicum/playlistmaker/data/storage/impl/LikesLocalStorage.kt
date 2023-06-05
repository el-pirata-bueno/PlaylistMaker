package com.practicum.playlistmaker.data.storage.impl

import android.content.SharedPreferences
import com.practicum.playlistmaker.data.storage.LikesStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class LikesLocalStorage(
    //private val sharedPreferences: SharedPreferences
    ): LikesStorage, KoinComponent {
    private companion object {
        const val LIKED_KEY = "LIKED_KEY"
    }
    val sharedPreferences: SharedPreferences by inject(qualifier = named("likesPrefs"))

    override fun likeTrack(trackId: Int) {
        changeLiked(trackId = trackId, remove = false)
    }

    override fun unlikeTrack(trackId: Int) {
        changeLiked(trackId = trackId, remove = true)
    }

    override fun getLiked(): Set<String> {
        return sharedPreferences.getStringSet(LIKED_KEY, emptySet()) ?: emptySet()
    }

    private fun changeLiked(trackId: Int, remove: Boolean) {
        val mutableSet = getLiked().toMutableSet()
        val modified =
            if (remove) mutableSet.remove(trackId.toString()) else mutableSet.add(trackId.toString())
        if (modified) sharedPreferences.edit().putStringSet(LIKED_KEY, mutableSet).apply()
    }
}
