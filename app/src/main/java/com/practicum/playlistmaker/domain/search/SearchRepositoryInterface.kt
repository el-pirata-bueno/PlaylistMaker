package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.models.Track

interface SearchRepositoryInterface {

    fun search(query: String, onSuccess: (List<Track>) -> Unit, onError: () -> Unit)

}