package com.practicum.playlistmaker.data.storage

import com.practicum.playlistmaker.domain.settings.model.ThemeSettings

interface SettingsStorage {

    fun getThemeAppSettings(): ThemeSettings
    fun getThemeSystemSettings(): ThemeSettings
    fun updateThemeAppSetting(settings: ThemeSettings)
    fun updateThemeSystemSetting(settings: ThemeSettings)

}