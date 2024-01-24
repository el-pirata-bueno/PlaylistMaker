package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchTracks(term: String): Flow<Resource<List<Track>>>
}