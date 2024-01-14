package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun clearHistory()
    suspend fun getHistoryTracks(): List<Track>
    suspend fun addTrackToHistory(track: Track)
    fun getTracks(term: String): Flow<Pair<List<Track>?, String?>>
}
