package com.practicum.playlistmaker.data.media.impl

import com.practicum.playlistmaker.data.converters.PlaylistMapper
import com.practicum.playlistmaker.data.converters.TrackMapper
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.media.MediaPlaylistsRepository
import com.practicum.playlistmaker.data.model.PlaylistEntity
import com.practicum.playlistmaker.data.model.TrackInPlaylistEntity
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MediaPlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistMapper: PlaylistMapper,
    private val trackMapper: TrackMapper,
) : MediaPlaylistsRepository {

    override suspend fun createPlaylist(playlistEntity: PlaylistEntity) {
        appDatabase.playlistDao().createPlaylist(playlistEntity)
    }

    override suspend fun updatePlaylist(playlistEntity: PlaylistEntity) {
        appDatabase.playlistDao().updatePlaylist(playlistEntity)
    }

    override suspend fun insertTrackInPlaylist(track: Track, playlist: Playlist) {
        appDatabase.trackInPlaylistDao().insertTrackInPlaylist(trackMapper.mapTrackToTrackInPlaylistEntity(track))

        val currentPlayListListTracks = playlist.listTracks.toMutableList()
        currentPlayListListTracks.add(track.trackId)

        val currentPlaylistCopy = playlist.copy(listTracks = currentPlayListListTracks, numTracks = currentPlayListListTracks.size)
        appDatabase.playlistDao().updatePlaylist(playlistMapper.mapPlaylistToEntity(currentPlaylistCopy))
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist): Playlist {
        var currentPlaylist = playlist
        val listToDeleteTrackIds: MutableList<Long> = mutableListOf()
        listToDeleteTrackIds.add(track.trackId)

        val currentPlayListListTrackIds = currentPlaylist.listTracks.toMutableList()

        for (trackId in currentPlayListListTrackIds) {
            if (trackId == track.trackId) {
                currentPlayListListTrackIds.remove(trackId)
                break
            }
        }

        val currentPlaylistCopy = currentPlaylist.copy(listTracks = currentPlayListListTrackIds, numTracks = currentPlayListListTrackIds.size)
        appDatabase.playlistDao().updatePlaylist(playlistMapper.mapPlaylistToEntity(currentPlaylistCopy))

        deleteTrackIfNotInPlaylists(listToDeleteTrackIds)

        return currentPlaylist
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        val currentPlayListListTrackIds = playlist.listTracks

        appDatabase.playlistDao().deletePlaylist(playlistMapper.mapPlaylistToEntity(playlist))

        deleteTrackIfNotInPlaylists(currentPlayListListTrackIds)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return playlistMapper.mapEntityToPlaylist(appDatabase.playlistDao().getPlaylistById(playlistId))
    }

    override suspend fun getPlaylistTracks(trackIds: List<Long>): Flow<List<Track>> = flow {
        val tracksInPlaylists = appDatabase.trackInPlaylistDao().getTracksInPlaylists()
        val playlistTracks = formPlaylistFromTracks(trackIds, tracksInPlaylists)
        emit(convertFromTrackEntity(playlistTracks))
    }

    private fun formPlaylistFromTracks(trackIds: List<Long>, tracksInPlaylists: List<TrackInPlaylistEntity>): MutableList<TrackInPlaylistEntity> {
        val playlistTrackEntities: MutableList<TrackInPlaylistEntity> = mutableListOf()
        for (trackId in trackIds) {
            for (track in tracksInPlaylists) {
                if (trackId == track.trackId) {
                    playlistTrackEntities.add(track)
                }
            }
        }
        return playlistTrackEntities
    }

    private suspend fun deleteTrackIfNotInPlaylists(listTrackIds: List<Long>) {
        val listPlaylists: MutableList<Playlist> = mutableListOf()
        getPlaylists().collect { playlists -> listPlaylists.addAll(playlists) }

        for (trackId in listTrackIds) {
            var isTrackInOtherPlaylists = false
            if (listPlaylists.isNotEmpty()) {
                for (playlist in listPlaylists) {
                    for (trackInPlaylistId in playlist.listTracks)
                        if (trackId == trackInPlaylistId) {
                            isTrackInOtherPlaylists = true
                            break
                        }
                    if (isTrackInOtherPlaylists) break
                }
                if (!isTrackInOtherPlaylists) {
                    appDatabase.trackInPlaylistDao().deleteTrackFromPlaylist(trackId)
                }
            }
            else {
                appDatabase.trackInPlaylistDao().deleteTrackFromPlaylist(trackId)
            }
        }
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistMapper.mapEntityToPlaylist(playlist) }
    }

    private fun convertFromTrackEntity(tracks: List<TrackInPlaylistEntity>): List<Track> {
        return tracks.map { track -> trackMapper.mapTrackInPlaylistEntitytoTrack(track) }
    }

}