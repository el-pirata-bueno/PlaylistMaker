package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchTracks(term: String): Flow<Resource<List<Track>>>
    fun getTrack(trackId: Int): Flow<Resource<List<Track>>>
}