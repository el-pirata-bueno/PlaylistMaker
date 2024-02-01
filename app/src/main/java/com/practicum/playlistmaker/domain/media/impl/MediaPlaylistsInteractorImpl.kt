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

    override suspend fun updatePlaylist(playlist: Playlist) {
        mediaPlaylistsRepository.updatePlaylist(playlistMapper.mapPlaylistToEntity(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        mediaPlaylistsRepository.deletePlaylist(playlist)
    }

    override suspend fun insertTrackInPlaylist(track: Track, playlist: Playlist) {
        mediaPlaylistsRepository.insertTrackInPlaylist(track, playlist)
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist) : Playlist {
        return mediaPlaylistsRepository.deleteTrackFromPlaylist(track, playlist)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return mediaPlaylistsRepository.getPlaylists()
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return mediaPlaylistsRepository.getPlaylistById(playlistId)
    }

    override suspend fun getPlaylistTracks(trackIds: List<Long>): Flow<List<Track>> {
        return mediaPlaylistsRepository.getPlaylistTracks(trackIds)
    }
}