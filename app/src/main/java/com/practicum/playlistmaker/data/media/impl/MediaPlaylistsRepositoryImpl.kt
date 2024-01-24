package com.practicum.playlistmaker.data.media.impl

import com.practicum.playlistmaker.data.converters.PlaylistMapper
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.media.MediaPlaylistsRepository
import com.practicum.playlistmaker.data.model.PlaylistEntity
import com.practicum.playlistmaker.data.model.TrackInPlaylistEntity
import com.practicum.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MediaPlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistMapper: PlaylistMapper
) : MediaPlaylistsRepository {

    override suspend fun createPlaylist(playlistEntity: PlaylistEntity) {
        appDatabase.playlistDao().createPlaylist(playlistEntity)
    }

    override suspend fun updatePlaylist(trackInPlaylistEntity: TrackInPlaylistEntity, playlistEntity: PlaylistEntity) {

        appDatabase.trackInPlaylistDao().insertTrackInPlaylist(trackInPlaylistEntity)
        appDatabase.playlistDao().updatePlaylist(playlistEntity)
    }

    override suspend fun deletePlaylist(playlistEntity: PlaylistEntity) {
        appDatabase.playlistDao().deletePlaylist(playlistEntity)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val likedTracks = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(likedTracks))
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistMapper.mapEntityToPlaylist(playlist) }
    }

}