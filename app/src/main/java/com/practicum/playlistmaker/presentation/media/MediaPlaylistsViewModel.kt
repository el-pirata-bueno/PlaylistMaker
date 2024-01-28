package com.practicum.playlistmaker.presentation.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.media.MediaPlaylistsInteractor
import com.practicum.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MediaPlaylistsViewModel(
    private val mediaPlaylistsInteractor: MediaPlaylistsInteractor
): ViewModel() {

    private var mediaPlaylistsStateLiveData = MutableLiveData<MediaPlaylistsState>()
    fun getMediaPlaylistsStateLiveData(): LiveData<MediaPlaylistsState> = mediaPlaylistsStateLiveData

    fun fillData() {
        renderState(MediaPlaylistsState.Empty)
        viewModelScope.launch(Dispatchers.IO) {
            mediaPlaylistsInteractor
                .getPlaylists()
                .collect {playlists ->
                    processResult(playlists)
                }
        }
    }

    fun onResume() {
        viewModelScope.launch(Dispatchers.IO) {
            mediaPlaylistsInteractor
                .getPlaylists()
                .collect {playlists ->
                    processResult(playlists)
                }
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            renderState(MediaPlaylistsState.Empty)
        } else {
            renderState(MediaPlaylistsState.Content(playlists))
        }
    }
    private fun renderState(state: MediaPlaylistsState) {
        mediaPlaylistsStateLiveData.postValue(state)
    }
}