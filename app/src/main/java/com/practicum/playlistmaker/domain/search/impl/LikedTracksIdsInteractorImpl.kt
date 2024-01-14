package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.domain.db.LikedTracksIdsInteractor
import com.practicum.playlistmaker.domain.db.LikedTracksIdsRepository

class LikedTracksIdsInteractorImpl(private val likedTracksIdsRepository: LikedTracksIdsRepository) :
    LikedTracksIdsInteractor {

    override suspend fun getLikedTracksIds(): List<Long> {
        return likedTracksIdsRepository.getLikedTracksIds()
    }
}