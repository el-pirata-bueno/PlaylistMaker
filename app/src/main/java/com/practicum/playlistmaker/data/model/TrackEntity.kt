package com.practicum.playlistmaker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val trackId: Long,
    val trackName: String?,
    val artistName: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val trackTime: String?,
    val artworkUrl100: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    val isFavorite: Boolean,
    val isInPlaylist: Boolean
)
