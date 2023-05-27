package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.models.Track

interface SearchInteractor {
    fun clearHistory()
    fun getHistoryTracks(): List<Track>
    fun addTrackToHistory(trackId: Int)
    //fun addTrack(track: Track)

    fun getTracks(term: String, consumer: GetTracksConsumer)
    interface GetTracksConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }

    fun getOneTrack(trackId: Int, consumer: GetOneTrackConsumer)
    interface GetOneTrackConsumer {
        fun consume(foundTrack: List<Track>?, errorMessage: String?)
    }
}
