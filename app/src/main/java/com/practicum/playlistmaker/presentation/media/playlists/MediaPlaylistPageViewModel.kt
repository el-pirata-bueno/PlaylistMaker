package com.practicum.playlistmaker.presentation.media.playlists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.media.MediaPlaylistsInteractor
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.ui.model.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

private const val UPDATE_PLAYLISTS_DELAY_TIME_MILLIS = 200L
private const val MILLIS_IN_MINUTE = 60000
class MediaPlaylistPageViewModel(
    val playlistId: Int,
    application: Application,
    private val mediaPlaylistsInteractor: MediaPlaylistsInteractor,
    private val sharingInteractor: SharingInteractor
) : AndroidViewModel(application) {

    private var mediaPlaylistPageStateLiveData = MutableLiveData<MediaPlaylistPageState>()
    fun getMediaPlaylistPageStateLiveData(): LiveData<MediaPlaylistPageState> = mediaPlaylistPageStateLiveData
    private val showToast = SingleLiveEvent<String?>()
    fun observeShowToast(): LiveData<String?> = showToast

    private var currentPlaylist: Playlist? = null
    private var currentPlaylistLength = 0
    private var tracksInPlaylist: MutableList<Track> = mutableListOf()

    init {
        getPlaylistById(playlistId)
        mediaPlaylistPageStateLiveData.postValue(MediaPlaylistPageState.PreLoading)
    }

    fun onResume() {
        viewModelScope.launch(Dispatchers.IO) {
            getPlaylistById(playlistId)
        }
    }

    fun deleteTrackFromPlaylist(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            mediaPlaylistsInteractor.deleteTrackFromPlaylist(track, currentPlaylist!!)
            delay(UPDATE_PLAYLISTS_DELAY_TIME_MILLIS)
            getPlaylistById(currentPlaylist!!.playlistId)
        }
    }
    fun sharePlaylist(typeText: String) {
        if (currentPlaylist != null) {
            var messageText = ""
            var numTrack = 0
            if (currentPlaylist!!.listTracks.isEmpty()) {
                val toastText =
                    getApplication<Application>().resources.getString(R.string.share_no_tracks)
                showToast.postValue(toastText)
            }
            else {
                val countTracks = getApplication<Application>().resources
                    .getQuantityString(
                        R.plurals.plurals_track,
                        currentPlaylist!!.numTracks,
                        currentPlaylist!!.numTracks
                    )
                messageText += String.format(
                    getApplication<Application>()
                        .resources.getString(R.string.share_playlist),
                    currentPlaylist!!.name,
                        currentPlaylist!!.description,
                        countTracks
                    )
                for (track in tracksInPlaylist) {
                    numTrack ++
                    messageText += String.format(
                        getApplication<Application>()
                            .resources.getString(R.string.share_track_in_playlist),
                            numTrack,
                            track.artistName,
                            track.trackName,
                            SimpleDateFormat("mm:ss", Locale.getDefault())
                                .format(track.trackTimeMillis)
                    )
                }
                sharingInteractor.sharePlaylist(messageText, typeText)
            }
        }
    }

    fun openMenu() {
        mediaPlaylistPageStateLiveData.postValue(MediaPlaylistPageState.MenuBottomSheet)
    }

    fun deletePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            mediaPlaylistsInteractor.deletePlaylist(currentPlaylist!!)
            currentPlaylistLength = 0
            mediaPlaylistPageStateLiveData.postValue(MediaPlaylistPageState
                .Content(currentPlaylist, tracksInPlaylist, currentPlaylistLength))
        }
    }

    private fun getPlaylistById(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            currentPlaylist = mediaPlaylistsInteractor.getPlaylistById(playlistId)
            mediaPlaylistsInteractor
                .getPlaylistTracks(currentPlaylist!!.listTracks)
                .collect { tracks ->
                    tracksInPlaylist.clear()
                    tracksInPlaylist.addAll(tracks) }
            if (tracksInPlaylist.isEmpty()) {
                currentPlaylistLength = 0
                mediaPlaylistPageStateLiveData.postValue(MediaPlaylistPageState
                    .Content(currentPlaylist, tracksInPlaylist, currentPlaylistLength))
            }
            else {
                currentPlaylistLength = countPlaylistLength(tracksInPlaylist)
                mediaPlaylistPageStateLiveData.postValue(MediaPlaylistPageState
                    .Content(currentPlaylist, tracksInPlaylist, currentPlaylistLength))
            }
        }
    }

    private fun countPlaylistLength (tracksInPlaylist: MutableList<Track>): Int {
        var totalLengthInMillis = 0

        if (tracksInPlaylist.isNotEmpty()) {
            for (track in tracksInPlaylist) {
                if (track.trackTimeMillis != null) {
                    totalLengthInMillis += track.trackTimeMillis
                }
            }
        }

        return totalLengthInMillis / MILLIS_IN_MINUTE
    }
}