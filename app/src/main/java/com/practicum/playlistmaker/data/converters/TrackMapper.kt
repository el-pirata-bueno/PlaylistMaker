package com.practicum.playlistmaker.data.converters

import com.practicum.playlistmaker.data.model.TrackEntity
import com.practicum.playlistmaker.data.model.TrackInPlaylistEntity
import com.practicum.playlistmaker.domain.model.Track

class TrackMapper {
    fun mapEntityToTrack(track: TrackEntity): Track {
        return with(track) {
            Track(
                trackId,
                trackName,
                artistName,
                collectionName,
                releaseDate,
                trackTimeMillis,
                artworkUrl100,
                primaryGenreName,
                country,
                previewUrl,
                isFavorite)
        }
    }

    fun mapTrackToEntity(track: Track): TrackEntity {
        return with(track) {
            TrackEntity(
                trackId,
                trackName,
                artistName,
                collectionName,
                releaseDate,
                trackTimeMillis,
                artworkUrl100,
                primaryGenreName,
                country,
                previewUrl,
                isFavorite,
                System.currentTimeMillis()
            )
        }
    }

    fun mapTrackToTrackInPlaylistEntity(track: Track): TrackInPlaylistEntity {
        return with(track) {
            TrackInPlaylistEntity(
                trackId,
                trackName,
                artistName,
                collectionName,
                releaseDate,
                trackTimeMillis,
                artworkUrl100,
                primaryGenreName,
                country,
                previewUrl,
                isFavorite,
                System.currentTimeMillis()
            )
        }
    }

    fun mapTrackInPlaylistEntitytoTrack(track: TrackInPlaylistEntity): Track {
        return with(track) {
            Track(
                trackId,
                trackName,
                artistName,
                collectionName,
                releaseDate,
                trackTimeMillis,
                artworkUrl100,
                primaryGenreName,
                country,
                previewUrl,
                isFavorite
            )
        }
    }
}
