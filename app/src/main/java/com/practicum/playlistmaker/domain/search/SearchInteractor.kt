package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun clearHistory()
    fun getHistoryTracks(): List<Track>
    fun addTrackToHistory(track: Track)

    fun getTracks(term: String): Flow<Pair<List<Track>?, String?>>
    fun getOneTrack(trackId: Int): Flow<Pair<List<Track>?, String?>>
}
