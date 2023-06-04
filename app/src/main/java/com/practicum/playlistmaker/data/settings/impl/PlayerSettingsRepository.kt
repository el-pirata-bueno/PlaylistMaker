package com.practicum.playlistmaker.data.settings.impl

import com.practicum.playlistmaker.data.settings.SettingsRepository
import com.practicum.playlistmaker.data.storage.impl.SettingsLocalStorage
import com.practicum.playlistmaker.domain.settings.model.ThemeSettings

class PlayerSettingsRepository(
    private val localStorage: SettingsLocalStorage
) : SettingsRepository {

    override fun getThemeAppSettings(): ThemeSettings = localStorage.getThemeAppSettings()
    override fun getThemeSystemSettings(): ThemeSettings = localStorage.getThemeSystemSettings()

    override fun updateThemeAppSetting(settings: ThemeSettings) {
        localStorage.updateThemeAppSetting(settings)
    }

    override fun updateThemeSystemSetting(settings: ThemeSettings) {
        localStorage.updateThemeSystemSetting(settings)
    }
}