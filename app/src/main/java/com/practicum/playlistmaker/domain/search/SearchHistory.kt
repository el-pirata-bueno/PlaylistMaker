package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.models.Track

interface SearchHistory {

    fun getHistory(): List<Track>
    fun saveHistory(tracks: List<Track>)
    fun clearHistory()
    fun addTrackToHistory(track: Track)

}