package com.practicum.playlistmaker.data.storage

import com.practicum.playlistmaker.domain.model.Track

interface PlaylistsStorage {

    fun addTrackToPlaylist(track: Track)
    fun removeTrackFromPlaylist(track: Track)
    fun getPlaylists(): Set<String>

}