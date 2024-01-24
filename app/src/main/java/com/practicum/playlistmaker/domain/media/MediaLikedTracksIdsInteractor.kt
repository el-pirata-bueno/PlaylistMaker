package com.practicum.playlistmaker.domain.media

interface MediaLikedTracksIdsInteractor {
    suspend fun getLikedTracksIds(): List<Long>
}