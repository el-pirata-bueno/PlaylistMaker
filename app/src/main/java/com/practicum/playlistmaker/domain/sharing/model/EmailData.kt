package com.practicum.playlistmaker.domain.sharing.model

data class EmailData(
    val data: String,
    val emailAddress: String,
    val emailSubject: String,
    val emailText: String
)