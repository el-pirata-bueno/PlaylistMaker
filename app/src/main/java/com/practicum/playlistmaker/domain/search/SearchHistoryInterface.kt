package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.models.Track

interface SearchHistoryInterface {

    fun read(): List<Track>

    fun write(tracks: List<Track>)

    fun delete()

    fun addTrack(track: Track)

}