package com.practicum.playlistmaker.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackDto(
    val trackName: String?,
    val artistName: String,
    val trackId: Int,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val isLiked: Boolean,
    val isFavourite: Boolean
) : Parcelable
