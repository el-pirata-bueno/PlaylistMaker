package com.practicum.playlistmaker.presentation.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MediaPlaylistsViewModel: ViewModel() {
    private var mediaPlaylistsStateLiveData = MutableLiveData<MediaPlaylistsState>()
    fun getMediaPlaylistsStateLiveData(): LiveData<MediaPlaylistsState> = mediaPlaylistsStateLiveData
}