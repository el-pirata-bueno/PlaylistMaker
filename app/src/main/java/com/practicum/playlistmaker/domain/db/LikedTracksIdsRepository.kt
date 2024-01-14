package com.practicum.playlistmaker.domain.db

interface LikedTracksIdsRepository {
    suspend fun getLikedTracksIds(): List<Long>
}