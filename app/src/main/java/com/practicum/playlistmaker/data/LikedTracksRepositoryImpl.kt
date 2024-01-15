package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.converters.TrackMapper
import com.practicum.playlistmaker.data.db.LikedTracksDatabase
import com.practicum.playlistmaker.data.model.TrackEntity
import com.practicum.playlistmaker.domain.db.LikedTracksRepository
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LikedTracksRepositoryImpl(
    private val appDatabase: LikedTracksDatabase,
    private val trackMapper: TrackMapper,
) : LikedTracksRepository {

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

    private fun convertFromTrackEntity(movies: List<TrackEntity>): List<Track> {
        return movies.map { movie -> trackMapper.map(movie) }
    }

}