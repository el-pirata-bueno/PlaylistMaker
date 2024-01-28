package com.practicum.playlistmaker.domain.media

import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface MediaLikedTracksInteractor {
    suspend fun getLikedTracks(): Flow<List<Track>>
}