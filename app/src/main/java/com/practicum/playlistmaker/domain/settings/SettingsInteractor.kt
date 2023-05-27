package com.practicum.playlistmaker.domain.settings

import com.practicum.playlistmaker.domain.settings.model.ThemeSettings

interface SettingsInteractor {
    fun getThemeAppSettings(): ThemeSettings
    fun updateThemeAppSetting(settings: ThemeSettings)
    fun getThemeSystemSettings(): ThemeSettings
    fun updateThemeSystemSetting(settings: ThemeSettings)
}