package com.practicum.playlistmaker.data.converters

import com.practicum.playlistmaker.data.model.TrackEntity
import com.practicum.playlistmaker.domain.model.Track

class TrackMapper {
    fun map(track: TrackEntity): Track {
        return with(track) {
            Track(
                trackId,
                trackName,
                artistName,
                collectionName,
                releaseDate,
                trackTime,
                artworkUrl100,
                primaryGenreName,
                country,
                previewUrl,
                isFavorite,
                isInPlaylist)
        }

    }
}
