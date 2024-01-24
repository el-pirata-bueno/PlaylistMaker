package com.practicum.playlistmaker.domain.media.impl

import com.practicum.playlistmaker.data.media.MediaLikedTracksRepository
import com.practicum.playlistmaker.domain.media.MediaLikedTracksInteractor
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class MediaLikedTracksInteractorImpl(private val mediaLikedTracksRepository: MediaLikedTracksRepository) :
    MediaLikedTracksInteractor {

    override suspend fun getLikedTracks(): Flow<List<Track>> {
        return mediaLikedTracksRepository.getLikedTracks()
    }
}