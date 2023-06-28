package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.SearchHistory
import com.practicum.playlistmaker.domain.search.SearchInteractor
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlayerSearchInteractor(
    private val repository: SearchRepository,
    private val searchHistory: SearchHistory
) : SearchInteractor {

    override fun clearHistory() {
        searchHistory.clearHistory()
    }

    override fun getHistoryTracks(): List<Track> {
        return searchHistory.getHistory()
    }

    override fun addTrackToHistory(track: Track) {
        searchHistory.addTrackToHistory(track)
    }

    override fun getTracks(term: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(term).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun getOneTrack(trackId: Int): Flow<Pair<List<Track>?, String?>> {
        return repository.getTrack(trackId).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}
