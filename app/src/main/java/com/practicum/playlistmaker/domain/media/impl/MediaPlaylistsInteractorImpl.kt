package com.practicum.playlistmaker.domain.media.impl

import com.practicum.playlistmaker.data.converters.PlaylistMapper
import com.practicum.playlistmaker.data.converters.TrackMapper
import com.practicum.playlistmaker.data.media.MediaPlaylistsRepository
import com.practicum.playlistmaker.domain.media.MediaPlaylistsInteractor
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class MediaPlaylistsInteractorImpl(
    private val mediaPlaylistsRepository: MediaPlaylistsRepository,
    private val trackMapper: TrackMapper,
    private val playlistMapper: PlaylistMapper
) : MediaPlaylistsInteractor {

    override suspend fun createPlaylist(playlist: Playlist) {
        mediaPlaylistsRepository.createPlaylist(playlistMapper.mapPlaylistToEntity(playlist))
    }

    override suspend fun updatePlaylist(track: Track, playlist: Playlist) {
        mediaPlaylistsRepository.updatePlaylist(trackMapper.mapTrackToTrackInPlaylistEntity(track), playlistMapper.mapPlaylistToEntity(playlist))
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return mediaPlaylistsRepository.getPlaylists()
    }
}