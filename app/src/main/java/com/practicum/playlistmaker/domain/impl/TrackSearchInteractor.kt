package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.SearchInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.SearchHistory
import com.practicum.playlistmaker.domain.search.SearchRepository

class TrackSearchInteractor(
    private val searchHistory: SearchHistory,
    private val repository: SearchRepository
) : SearchInteractor {
    override fun delete() {
        searchHistory.delete()
    }

    override fun read(): List<Track> {
        return searchHistory.read()
    }

    override fun addTrack(track: Track) {
        //на перспективу
    }

    override fun saveTrack(track: Track) {
        searchHistory.addTrack(track)
    }

    override fun search(query: String, onSuccess: (List<Track>) -> Unit, onError: () -> Unit) {
        repository.search(query, onSuccess, onError)
    }
}