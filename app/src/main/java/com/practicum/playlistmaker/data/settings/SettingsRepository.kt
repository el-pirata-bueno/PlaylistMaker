package com.practicum.playlistmaker.data.settings

import com.practicum.playlistmaker.domain.settings.model.ThemeSettings

interface SettingsRepository {
    fun getThemeAppSettings(): ThemeSettings
    fun updateThemeAppSetting(settings: ThemeSettings)
    fun getThemeSystemSettings(): ThemeSettings
    fun updateThemeSystemSetting(settings: ThemeSettings)
}