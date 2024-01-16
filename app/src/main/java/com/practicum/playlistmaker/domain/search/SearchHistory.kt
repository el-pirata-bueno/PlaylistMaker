package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.model.Track

interface SearchHistory {
    suspend fun getHistory(): List<Track>
    fun saveHistory(tracks: List<Track>)
    fun clearHistory()
    suspend fun addTrackToHistory(track: Track)
}