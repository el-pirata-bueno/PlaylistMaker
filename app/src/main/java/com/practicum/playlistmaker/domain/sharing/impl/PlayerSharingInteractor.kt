package com.practicum.playlistmaker.domain.sharing.impl

import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.model.EmailData

class PlayerSharingInteractor(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp(shareUrl: String, typeText: String) {
        externalNavigator.shareApp(shareUrl, typeText)
    }

    override fun openSupport(email: EmailData) {
        externalNavigator.openSupport(email)
    }

    override fun openTerms(termsUrl: String) {
        externalNavigator.openTerms(termsUrl)
    }

    /* Эти методы видимо для случая, когда url и прочие вещи хранятся в БД, а не в ресурсах
    private fun getShareAppLink(): String {
        return R.string.share_button_link.toString()
    }

    private fun getSupportEmailData(): EmailData {
        val email = EmailData()
        return email
    }

    private fun getTermsLink(): String {
        return getString(R.string.agreement_link)
    }
    */
}
