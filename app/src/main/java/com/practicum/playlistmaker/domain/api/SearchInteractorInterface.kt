package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface SearchInteractorInterface {
    fun delete()
    fun read(): List<Track>
    fun addTrack(track: Track)
    fun saveTrack(track: Track)
    fun search(query: String, onSuccess: (List<Track>) -> Unit, onError: () -> Unit)
}
