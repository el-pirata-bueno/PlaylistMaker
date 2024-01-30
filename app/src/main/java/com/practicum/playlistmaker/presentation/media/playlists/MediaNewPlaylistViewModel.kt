package com.practicum.playlistmaker.presentation.media.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.media.MediaPlaylistsInteractor
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.ui.media.playlists.MediaNewEditPlaylistState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class MediaNewPlaylistViewModel(
    open val playlistId: Int?,
    val mediaPlaylistsInteractor: MediaPlaylistsInteractor
): ViewModel() {

    var playlistNewEditStateLiveData = MutableLiveData<MediaNewEditPlaylistState>()
    fun getPlaylistNewEditStateLiveData(): LiveData<MediaNewEditPlaylistState> = playlistNewEditStateLiveData

    init {
        playlistNewEditStateLiveData.postValue(MediaNewEditPlaylistState.NewPlaylist)
    }
    fun createPlaylist(name: String, description: String?, coverFilePath: String?) {
        var currentPlaylist: Playlist = mapArgsToPlaylist(name, description, coverFilePath)

        viewModelScope.launch(Dispatchers.IO) {
            mediaPlaylistsInteractor.createPlaylist(currentPlaylist)
        }
    }

    private fun mapArgsToPlaylist(
        name: String,
        description: String?,
        coverFilePath: String?
    ) : Playlist {
        val listTracks = mutableListOf<Long>()
        return Playlist (0, name, description, coverFilePath,
            listTracks, 0, System.currentTimeMillis())
    }
}