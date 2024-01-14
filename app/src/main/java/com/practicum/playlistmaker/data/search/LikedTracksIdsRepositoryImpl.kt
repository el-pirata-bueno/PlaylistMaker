package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.data.db.LikedTracksDatabase
import com.practicum.playlistmaker.domain.db.LikedTracksIdsRepository

class LikedTracksIdsRepositoryImpl(
    private val appDatabase: LikedTracksDatabase,
) : LikedTracksIdsRepository {
    override suspend fun getLikedTracksIds(): List<Long> {
        return appDatabase.trackDao().getTrackIds()
    }
}