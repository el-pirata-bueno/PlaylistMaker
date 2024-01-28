package com.practicum.playlistmaker.data.media.impl

import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.media.MediaLikedTracksIdsRepository

class MediaLikedTracksIdsRepositoryImpl(
    private val appDatabase: AppDatabase,
) : MediaLikedTracksIdsRepository {
    override suspend fun getLikedTracksIds(): List<Long> {
        return appDatabase.trackDao().getTrackIds()
    }
}