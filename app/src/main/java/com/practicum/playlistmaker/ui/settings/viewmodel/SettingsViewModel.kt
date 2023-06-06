package com.practicum.playlistmaker.ui.settings.viewmodel

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.model.ThemeSettings
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.model.EmailData

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    fun getThemeSystemSettings(): Boolean = settingsInteractor.getThemeSystemSettings().isActive
    fun getThemeAppSettings(): Boolean = settingsInteractor.getThemeAppSettings().isDarkTheme
    fun updateThemeAppSettings(settings: ThemeSettings) = settingsInteractor.updateThemeAppSetting(settings)
    fun updateThemeSystemSettings(settings: ThemeSettings) = settingsInteractor.updateThemeSystemSetting(settings)

    fun shareApp(shareUrl: String, typeText: String) = sharingInteractor.shareApp(shareUrl, typeText)
    fun openSupport(email: EmailData) = sharingInteractor.openSupport(email)
    fun openTerms(termsUrl: String) = sharingInteractor.openTerms(termsUrl)

}
