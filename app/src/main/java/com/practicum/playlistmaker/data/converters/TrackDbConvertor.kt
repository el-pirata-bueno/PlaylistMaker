package com.practicum.playlistmaker.data.converters

import com.practicum.playlistmaker.data.model.TrackEntity
import com.practicum.playlistmaker.domain.model.Track

class TrackDbConvertor {
    fun map(track: TrackEntity): Track {
        return Track(track.trackId, track.trackName, track.artistName, track.collectionName, track.releaseDate, track.trackTime, track.artworkUrl100, track.primaryGenreName, track.country, track.previewUrl, track.isFavorite, track.isInPlaylist)
    }
}
