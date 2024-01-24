package com.practicum.playlistmaker.presentation.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.media.MediaPlaylistsInteractor
import com.practicum.playlistmaker.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MediaNewPlaylistViewModel(
    private val mediaPlaylistsInteractor: MediaPlaylistsInteractor
): ViewModel() {
    private var mediaNewPlaylistStateLiveData = MutableLiveData<MediaNewPlaylistState>()
    fun getMediaPlaylistsStateLiveData(): LiveData<MediaNewPlaylistState> = mediaNewPlaylistStateLiveData
    fun createPlaylist(name: String, description: String?, coverFilePath: String?) {
        var currentPlaylist: Playlist = mapArgsToPlaylist(name, description, coverFilePath)

        viewModelScope.launch(Dispatchers.IO) {
            mediaPlaylistsInteractor.createPlaylist(currentPlaylist)
        }
    }

    init {
        mediaNewPlaylistStateLiveData.postValue(MediaNewPlaylistState.Content)
    }

    private fun mapArgsToPlaylist(
        name: String,
        description: String?,
        coverFilePath: String?
    ) : Playlist {
        val listTracks = mutableListOf<Long>()
        return Playlist (0, name, description, coverFilePath,
            listTracks, 0)
    }
}