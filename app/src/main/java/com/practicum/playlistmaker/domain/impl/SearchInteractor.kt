package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.SearchInteractorInterface
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.SearchHistoryInterface
import com.practicum.playlistmaker.domain.search.SearchRepositoryInterface

class SearchInteractor(
    private val searchHistory: SearchHistoryInterface,
    private val repository: SearchRepositoryInterface
) : SearchInteractorInterface {
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