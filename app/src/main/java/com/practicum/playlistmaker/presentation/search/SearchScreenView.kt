package com.practicum.playlistmaker.presentation.search

import com.practicum.playlistmaker.presentation.models.TrackUi

interface SearchScreenView {
    fun showHistory(tracksHistory: List<TrackUi>)
    fun showEmptyResult()
    fun showTracks(tracks: List<TrackUi>)
    fun showSearchError()
    fun clearSearchText()
    fun hideKeyboard()
    fun showLoading()
    fun clickDebounce(): Boolean
}