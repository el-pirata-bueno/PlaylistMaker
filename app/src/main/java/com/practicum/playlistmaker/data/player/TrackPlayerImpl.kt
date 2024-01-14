package com.practicum.playlistmaker.data.player

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.db.LikedTracksDatabase
import com.practicum.playlistmaker.data.model.TrackDto
import com.practicum.playlistmaker.data.model.TrackEntity
import com.practicum.playlistmaker.data.storage.impl.PlaylistsLocalStorage
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.player.TrackPlayer
import com.practicum.playlistmaker.domain.search.SearchHistory
import java.text.SimpleDateFormat
import java.util.Locale

class TrackPlayerImpl (
    private val appDatabase: LikedTracksDatabase,
    private val searchHistory: SearchHistory,
    private val playlistsLocalStorage: PlaylistsLocalStorage,
    private val mediaPlayer: MediaPlayer
) : TrackPlayer {

    override var playerState = MediaPlayerState.STATE_DEFAULT

    //val tracksInPlaylists = playlistsLocalStorage.getPlaylists()

    override fun preparePlayer(previewUrl: String) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = MediaPlayerState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = MediaPlayerState.STATE_PREPARED
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = MediaPlayerState.STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = MediaPlayerState.STATE_PAUSED
    }

    override fun releasePlayer() {
        mediaPlayer.reset()
    }

    override suspend fun likeTrack(track: Track) {
        appDatabase.trackDao().insertLikedTrack(mapTrackToEntity(track))
        searchHistory.addTrackToHistory(track)
    }

    override suspend fun unlikeTrack(track: Track) {
        appDatabase.trackDao().deleteLikedTrack(mapTrackToEntity(track))
        searchHistory.addTrackToHistory(track)
    }

    override fun addTrackToPlaylist(track: Track) {
        playlistsLocalStorage.addTrackToPlaylist(track)
    }

    override fun removeTrackFromPlaylist(track: Track) {
        playlistsLocalStorage.removeTrackFromPlaylist(track)
    }

    override fun getCurrentPosition(): Int = mediaPlayer.currentPosition
    override fun getTrackDuration(): Int = mediaPlayer.duration

    private fun mapTrack(
        isFavorite: Boolean,
        tracksInPlaylists: Set<String>,
        trackDto: TrackDto
    ): Track {
        return Track(
            trackName = trackDto.trackName ?: "",
            artistName = trackDto.artistName ?: "",
            trackId = trackDto.trackId ?: 0,
            trackTime = if (trackDto.trackTimeMillis != null)
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackDto.trackTimeMillis) else "",
            artworkUrl100 = trackDto.artworkUrl100 ?: "",
            collectionName = trackDto.collectionName ?: "",
            releaseDate = trackDto.releaseDate ?: "",
            primaryGenreName = trackDto.primaryGenreName ?: "",
            country = trackDto.country ?: "",
            previewUrl = trackDto.previewUrl ?: "",
            isFavorite = isFavorite,
            isInPlaylist = tracksInPlaylists.contains(trackDto.trackId.toString())
        )
    }

    private fun mapTrackToEntity(
        track: Track
    ): TrackEntity {
        return TrackEntity(
            trackName = track.trackName ?: "",
            artistName = track.artistName ?: "",
            trackId = track.trackId ?: 0,
            trackTime = track.trackTime ?: "",
            artworkUrl100 = track.artworkUrl100 ?: "",
            collectionName = track.collectionName ?: "",
            releaseDate = track.releaseDate ?: "",
            primaryGenreName = track.primaryGenreName ?: "",
            country = track.country ?: "",
            previewUrl = track.previewUrl ?: "",
            isFavorite = track.isFavorite,
            isInPlaylist = track.isInPlaylist,
            createdAt = System.currentTimeMillis()
        )
    }
}