package com.practicum.playlistmaker.data.media

import com.practicum.playlistmaker.data.model.PlaylistEntity
import com.practicum.playlistmaker.data.model.TrackInPlaylistEntity
import com.practicum.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface MediaPlaylistsRepository {
    suspend fun createPlaylist(playlistEntity: PlaylistEntity)
    suspend fun updatePlaylist(trackInPlaylistEntity: TrackInPlaylistEntity, playlistEntity: PlaylistEntity)
    suspend fun deletePlaylist(playlistEntity: PlaylistEntity)
    suspend fun getPlaylists(): Flow<List<Playlist>>
}