package com.practicum.playlistmaker.data.media

import com.practicum.playlistmaker.data.model.TrackEntity
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface MediaLikedTracksRepository {

    suspend fun deleteLikedTrack(trackEntity: TrackEntity)
    suspend fun insertLikedTrack(trackEntity: TrackEntity)
    suspend fun getLikedTracks(): Flow<List<Track>>
}