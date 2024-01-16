package com.practicum.playlistmaker.domain.db

import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface LikedTracksInteractor {

    suspend fun getLikedTracks(): Flow<List<Track>>
}