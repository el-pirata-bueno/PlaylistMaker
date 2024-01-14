package com.practicum.playlistmaker.domain.db

interface LikedTracksIdsInteractor {

    suspend fun getLikedTracksIds(): List<Long>
}