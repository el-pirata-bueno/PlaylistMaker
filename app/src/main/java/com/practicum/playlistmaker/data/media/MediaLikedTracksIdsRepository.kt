package com.practicum.playlistmaker.data.media

interface MediaLikedTracksIdsRepository {
    suspend fun getLikedTracksIds(): List<Long>
}