package com.practicum.playlistmaker.ui.models

data class PlayStatus(
    val progress: Float,    // прогресс трека в процентах от 0 до 100
    val isPlaying: Boolean  // на перспективу
)