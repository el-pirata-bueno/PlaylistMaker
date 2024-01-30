package com.practicum.playlistmaker.data.media

import com.practicum.playlistmaker.data.model.PlaylistEntity
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface MediaPlaylistsRepository {
    suspend fun createPlaylist(playlistEntity: PlaylistEntity)
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)
    suspend fun insertTrackInPlaylist(track: Track, playlist: Playlist)
    suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist): Playlist
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun getPlaylistById(playlistId: Int): Playlist
    suspend fun getPlaylistTracks(trackIds: List<Long>): Flow<List<Track>>
}