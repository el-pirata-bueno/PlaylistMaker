package com.practicum.playlistmaker.data.sharing

import com.practicum.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {
    fun shareApp(shareUrl: String, typeText: String)
    fun openSupport(email: EmailData)
    fun openTerms(link: String)
}