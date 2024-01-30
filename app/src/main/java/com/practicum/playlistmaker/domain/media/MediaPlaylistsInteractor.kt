package com.practicum.playlistmaker.domain.media

import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface MediaPlaylistsInteractor {
    suspend fun createPlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun insertTrackInPlaylist(track: Track, playlist: Playlist)
    suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist): Playlist
    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun getPlaylistById(playlistId: Int): Playlist
    suspend fun getPlaylistTracks(trackIds: List<Long>): Flow<List<Track>>
}