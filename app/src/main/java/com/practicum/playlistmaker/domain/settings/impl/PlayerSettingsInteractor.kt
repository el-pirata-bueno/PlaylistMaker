package com.practicum.playlistmaker.domain.settings.impl

import com.practicum.playlistmaker.data.settings.SettingsRepository
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.model.ThemeSettings

class PlayerSettingsInteractor(
    private val repository: SettingsRepository
) : SettingsInteractor {
    override fun getThemeAppSettings(): ThemeSettings = repository.getThemeAppSettings()
    override fun getThemeSystemSettings(): ThemeSettings = repository.getThemeSystemSettings()

    override fun updateThemeAppSetting(settings: ThemeSettings) {
        repository.updateThemeAppSetting(settings)
    }

    override fun updateThemeSystemSetting(settings: ThemeSettings) {
        repository.updateThemeSystemSetting(settings)
    }
}