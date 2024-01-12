package com.practicum.playlistmaker.data.storage

interface PlaylistsStorage {

    fun addTrackToPlaylist(trackId: Long)
    fun removeTrackFromPlaylist(trackId: Long)
    fun getPlaylists(): Set<String>

}