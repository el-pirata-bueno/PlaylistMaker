package com.practicum.playlistmaker.presentation.search

import com.practicum.playlistmaker.domain.impl.TrackSearchInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.models.TrackUi

class SearchPresenter(
    private val view: SearchScreenView,
    private val router: SearchRouter,
    private val searchInteractor: TrackSearchInteractor
) {

    private val cachedTracks = mutableListOf<Track>()

    fun arrowBackButtonClicked() {
        router.goBack()
    }

    fun onHistoryDeleteClicked() {
        searchInteractor.delete()
        view.showHistory(searchInteractor.read().map { mapTrackToUi(it) })
    }

    fun searchFocusChanged(hasFocus: Boolean, text: String) {
        val historyTracks = searchInteractor.read()
        if (hasFocus && text.isEmpty() && historyTracks.isNotEmpty()) {
            view.showHistory(historyTracks.map { mapTrackToUi(it) })
        } else {
            view.showTracks(cachedTracks.map { mapTrackToUi(it) })
        }
    }

    fun searchTextClearClicked() {
        val historyTracks = searchInteractor.read()
        view.clearSearchText()
        view.hideKeyboard()
        if (historyTracks.isNotEmpty()) {
            view.showHistory(historyTracks.map { mapTrackToUi(it) })
        }
    }

    fun search(query: String) {
        if (query.isEmpty()) {
            return
        }
        view.showLoading()
        searchInteractor.search(
            query = query,
            onSuccess = { tracks ->
                cachedTracks.clear()
                cachedTracks.addAll(tracks)
                if (tracks.isEmpty()) {
                    view.showEmptyResult()
                } else {
                    view.showTracks(tracks.map { mapTrackToUi(it) })
                }
            },
            onError = {
                view.showSearchError()
            }
        )
    }

    fun onTrackClicked(track: TrackUi) {
        //todo: move logic t
        if (view.clickDebounce()) {
            searchInteractor.saveTrack(mapTrackUiToTrack(track))
            router.openTrack(track)
        }

    }

    private fun mapTrackToUi(track: Track): TrackUi {
        return TrackUi(
            track.trackName,
            track.artistName,
            track.trackId,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isLiked,
            track.isFavourite
        )
    }

    private fun mapTrackUiToTrack(trackUi: TrackUi): Track {
        return Track(
            trackUi.trackName,
            trackUi.artistName,
            trackUi.trackId,
            trackUi.trackTimeMillis,
            trackUi.artworkUrl100,
            trackUi.collectionName,
            trackUi.releaseDate,
            trackUi.primaryGenreName,
            trackUi.country,
            trackUi.previewUrl,
            trackUi.isLiked,
            trackUi.isFavourite
        )
    }
}