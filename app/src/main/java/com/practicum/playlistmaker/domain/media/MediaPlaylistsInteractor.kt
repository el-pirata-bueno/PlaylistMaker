package com.practicum.playlistmaker.domain.media

import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface MediaPlaylistsInteractor {
    suspend fun createPlaylist(playlist: Playlist)
    suspend fun updatePlaylist(track: Track, playlist: Playlist)
    suspend fun getPlaylists(): Flow<List<Playlist>>
}