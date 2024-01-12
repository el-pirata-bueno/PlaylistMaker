package com.practicum.playlistmaker.domain.db

import com.practicum.playlistmaker.data.model.TrackEntity
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface LikedTracksRepository {

    suspend fun deleteLikedTrack(trackEntity: TrackEntity)
    suspend fun insertLikedTrack(trackEntity: TrackEntity)
    suspend fun getLikedTracks(): Flow<List<Track>>
}