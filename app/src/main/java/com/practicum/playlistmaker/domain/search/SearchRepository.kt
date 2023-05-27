package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.util.Resource

interface SearchRepository {

    fun searchTracks (term: String) : Resource<List<Track>>
    fun getTrack (trackId: Int) : Resource<List<Track>>

}