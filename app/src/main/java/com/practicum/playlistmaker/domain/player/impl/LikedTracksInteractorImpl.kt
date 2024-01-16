package com.practicum.playlistmaker.domain.player.impl

import com.practicum.playlistmaker.domain.db.LikedTracksInteractor
import com.practicum.playlistmaker.domain.db.LikedTracksRepository
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class LikedTracksInteractorImpl(private val likedTracksRepository: LikedTracksRepository) :
    LikedTracksInteractor {

    override suspend fun getLikedTracks(): Flow<List<Track>> {
        return likedTracksRepository.getLikedTracks()
    }
}