package com.practicum.playlistmaker.data.media.impl

import com.practicum.playlistmaker.data.converters.TrackMapper
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.media.MediaLikedTracksRepository
import com.practicum.playlistmaker.data.model.TrackEntity
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MediaLikedTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackMapper: TrackMapper,
) : MediaLikedTracksRepository {

    override suspend fun deleteLikedTrack(trackEntity: TrackEntity) {
        appDatabase.trackDao().deleteLikedTrack(trackEntity)
    }

    override suspend fun insertLikedTrack(trackEntity: TrackEntity) {
        appDatabase.trackDao().insertLikedTrack(trackEntity)
    }

    override suspend fun getLikedTracks(): Flow<List<Track>> = flow {
        val likedTracks = appDatabase.trackDao().getLikedTracks()
        emit(convertFromTrackEntity(likedTracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackMapper.mapEntityToTrack(track) }
    }

}