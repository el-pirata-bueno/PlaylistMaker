package com.practicum.playlistmaker.presentation.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.media.MediaLikedTracksInteractor
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MediaLikedTracksViewModel(
    private val mediaLikedTracksInteractor: MediaLikedTracksInteractor,
): ViewModel() {

    private var mediaLikedTracksStateLiveData = MutableLiveData<MediaLikedTracksState>()
    fun getMediaLikedStateLiveData(): LiveData<MediaLikedTracksState> = mediaLikedTracksStateLiveData

    fun fillData() {
        renderState(MediaLikedTracksState.Empty)
        viewModelScope.launch(Dispatchers.IO) {
            mediaLikedTracksInteractor
                .getLikedTracks()
                .collect {tracks ->
                    processResult(tracks)
                }
        }
    }

    fun onResume() {
        viewModelScope.launch(Dispatchers.IO) {
            mediaLikedTracksInteractor
                .getLikedTracks()
                .collect {tracks ->
                    processResult(tracks)
                }
        }
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            renderState(MediaLikedTracksState.Empty)
        } else {
            renderState(MediaLikedTracksState.Content(tracks))
        }
    }

    private fun renderState(state: MediaLikedTracksState) {
        mediaLikedTracksStateLiveData.postValue(state)
    }
}