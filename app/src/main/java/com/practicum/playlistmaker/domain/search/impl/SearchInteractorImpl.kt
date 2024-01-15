package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.search.SearchHistory
import com.practicum.playlistmaker.domain.search.SearchInteractor
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.util.ErrorType
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(
    private val repository: SearchRepository,
    private val searchHistory: SearchHistory
) : SearchInteractor {

    override fun clearHistory() {
        searchHistory.clearHistory()
    }

    override suspend fun getHistoryTracks(): List<Track> {
        return searchHistory.getHistory()
    }

    override suspend fun addTrackToHistory(track: Track) {
        searchHistory.addTrackToHistory(track)
    }

    override fun getTracks(term: String): Flow<Pair<List<Track>?, ErrorType?>> {
        return repository.searchTracks(term).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.errorType)
                }
            }
        }
    }

}
