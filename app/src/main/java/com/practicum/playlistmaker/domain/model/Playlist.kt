package com.practicum.playlistmaker.domain.model

data class Playlist(
    val playlistId: Int,
    val name: String,
    val description: String?,
    val imageLink: String?,
    val listTracks: MutableList<Long>,
    val numTracks: Int
)
