package com.practicum.playlistmaker.presentation.media

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.db.LikedTracksInteractor
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.launch

class MediaLikedViewModel(
    private val context: Context,
    private val likedTracksInteractor: LikedTracksInteractor
): ViewModel() {

    private var mediaLikedStateLiveData = MutableLiveData<MediaLikedState>()
    fun getMediaLikedStateLiveData(): LiveData<MediaLikedState> = mediaLikedStateLiveData

    fun fillData() {
        renderState(MediaLikedState.Empty(""))
        viewModelScope.launch {
            likedTracksInteractor
                .getLikedTracks()
                .collect {tracks ->
                    processResult(tracks)
                }
        }

    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            //"Переделать на обычный объект с захардкоженной строкой?"
            renderState(MediaLikedState.Empty(context.getString(R.string.media_empty)))
        } else {
            renderState(MediaLikedState.Content(tracks))
        }
    }

    private fun renderState(state: MediaLikedState) {
        mediaLikedStateLiveData.postValue(state)
    }
}