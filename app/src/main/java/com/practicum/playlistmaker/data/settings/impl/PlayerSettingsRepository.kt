package com.practicum.playlistmaker.data.settings.impl

import android.content.Context
import com.practicum.playlistmaker.data.settings.SettingsLocalStorage
import com.practicum.playlistmaker.data.settings.SettingsRepository
import com.practicum.playlistmaker.domain.settings.model.ThemeSettings

class PlayerSettingsRepository(
    private val context: Context,
    private val localStorage: SettingsLocalStorage
) : SettingsRepository {

    override fun getThemeAppSettings(): ThemeSettings {
        return localStorage.getThemeAppSettings()
    }

    override fun updateThemeAppSetting(settings: ThemeSettings) {
        localStorage.updateThemeAppSetting(settings)
    }

    override fun getThemeSystemSettings(): ThemeSettings {
        return localStorage.getThemeSystemSettings()
    }

    override fun updateThemeSystemSetting(settings: ThemeSettings) {
        localStorage.updateThemeSystemSetting(settings)
    }
}