package com.practicum.playlistmaker.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackUi(
    val trackName: String?,
    val artistName: String,
    val trackId: Int,
    val trackTime: String,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String?,
    var isLiked: Boolean,
    var isInPlaylist: Boolean
) : Parcelable

