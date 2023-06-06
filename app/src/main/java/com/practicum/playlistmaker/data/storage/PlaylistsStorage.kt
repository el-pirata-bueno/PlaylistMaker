package com.practicum.playlistmaker.data.storage

interface PlaylistsStorage {

    fun addTrackToPlaylist(trackId: Int)
    fun removeTrackFromPlaylist(trackId: Int)
    fun getPlaylists(): Set<String>

}