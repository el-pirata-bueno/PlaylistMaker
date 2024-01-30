package com.practicum.playlistmaker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int,
    val name: String,
    val description: String?,
    val imageLink: String?,
    val listTracks: String?,
    val numTracks: Int,
    val createdAt: Long
)
