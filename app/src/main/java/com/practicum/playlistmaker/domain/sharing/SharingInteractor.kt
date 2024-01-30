package com.practicum.playlistmaker.domain.sharing

import com.practicum.playlistmaker.domain.sharing.model.EmailData

interface SharingInteractor {
    fun shareApp(shareUrl: String, typeText: String)
    fun sharePlaylist(messageText: String, typeText: String)
    fun openTerms(termsUrl: String)
    fun openSupport(email: EmailData)
}
