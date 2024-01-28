package com.practicum.playlistmaker.domain.media.impl

import com.practicum.playlistmaker.data.media.MediaLikedTracksIdsRepository
import com.practicum.playlistmaker.domain.media.MediaLikedTracksIdsInteractor

class MediaLikedTracksIdsInteractorImpl(private val mediaLikedTracksIdsRepository: MediaLikedTracksIdsRepository) :
    MediaLikedTracksIdsInteractor {

    override suspend fun getLikedTracksIds(): List<Long> {
        return mediaLikedTracksIdsRepository.getLikedTracksIds()
    }
}