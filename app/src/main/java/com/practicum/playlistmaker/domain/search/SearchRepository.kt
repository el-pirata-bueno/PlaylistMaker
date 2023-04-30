package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.models.Track

interface SearchRepository {

    fun search(query: String, onSuccess: (List<Track>) -> Unit, onError: () -> Unit)

}