package com.practicum.playlistmaker.data.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.model.PlaylistEntity
import com.practicum.playlistmaker.domain.model.Playlist


class PlaylistMapper(private val gson: Gson) {
    fun mapEntityToPlaylist(playlist: PlaylistEntity): Playlist {
        val itemType = object : TypeToken<MutableList<Long>>() {}.type
        val listTracks = gson.fromJson<MutableList<Long>>(playlist.listTracks, itemType) ?: emptyList()

        return with(playlist) {
            Playlist(
                playlistId,
                name,
                description,
                imageLink,
                listTracks.toMutableList(),
                numTracks,
                createdAt
            )
        }
    }

    fun mapPlaylistToEntity(playlist: Playlist): PlaylistEntity {
        val listTracksEntity = gson.toJson(playlist.listTracks)


        return with(playlist) {
            PlaylistEntity(
                playlistId,
                name,
                description,
                imageLink,
                listTracksEntity,
                numTracks,
                createdAt
            )
        }
    }
}
