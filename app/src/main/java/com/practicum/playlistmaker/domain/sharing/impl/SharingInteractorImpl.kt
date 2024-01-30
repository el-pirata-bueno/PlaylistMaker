package com.practicum.playlistmaker.domain.sharing.impl

import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp(shareUrl: String, typeText: String) {
        externalNavigator.shareApp(shareUrl, typeText)
    }

    override fun sharePlaylist(messageText: String, typeText: String) {
        externalNavigator.sharePlaylist(messageText, typeText)
    }

    override fun openSupport(email: EmailData) {
        externalNavigator.openSupport(email)
    }

    override fun openTerms(termsUrl: String) {
        externalNavigator.openTerms(termsUrl)
    }
}
