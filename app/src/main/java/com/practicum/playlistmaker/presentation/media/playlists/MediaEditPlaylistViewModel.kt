package com.practicum.playlistmaker.presentation.media.playlists

import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.media.MediaPlaylistsInteractor
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.ui.media.playlists.MediaNewEditPlaylistState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MediaEditPlaylistViewModel(
    override val playlistId: Int?,
    mediaPlaylistsInteractor: MediaPlaylistsInteractor,
) : MediaNewPlaylistViewModel(playlistId, mediaPlaylistsInteractor)
    {
    private var currentPlaylist: Playlist? = null
        init {
            playlistNewEditStateLiveData.postValue(MediaNewEditPlaylistState.NewPlaylist)
            viewModelScope.launch(Dispatchers.IO) {
                currentPlaylist = mediaPlaylistsInteractor
                    .getPlaylistById(playlistId!!)
                playlistNewEditStateLiveData.postValue(MediaNewEditPlaylistState.EditPlaylist(currentPlaylist))
            }
        }
    fun updatePlaylist(name: String, description: String?, coverFilePath: String?) {
        val updatedPlaylist: Playlist = currentPlaylist!!.copy(name = name, description = description, imageLink = coverFilePath)

        viewModelScope.launch(Dispatchers.IO) {
            mediaPlaylistsInteractor.updatePlaylist(updatedPlaylist)
        }
    }
}