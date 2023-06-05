package com.practicum.playlistmaker.data.storage.impl

import android.content.SharedPreferences
import com.practicum.playlistmaker.data.storage.SettingsStorage
import com.practicum.playlistmaker.domain.settings.model.ThemeSettings
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class SettingsLocalStorage(
    //private val sharedPreferences: SharedPreferences
    ): SettingsStorage, KoinComponent {
    private companion object {
        const val APP_DARK_THEME = "APP_DARK_THEME"
        const val SYSTEM_THEME_ACTIVE = "SYSTEM_THEME_ACTIVE"
    }
    val sharedPreferences: SharedPreferences by inject(qualifier = named("settingsPrefs"))

    override fun getThemeAppSettings(): ThemeSettings = ThemeSettings(false, sharedPreferences.getBoolean(
        APP_DARK_THEME, false))

    override fun getThemeSystemSettings(): ThemeSettings {
        val themeSystemActive =
            ThemeSettings(sharedPreferences.getBoolean(SYSTEM_THEME_ACTIVE, false), false)
        if (themeSystemActive.isActive) {
            sharedPreferences.edit().putBoolean(APP_DARK_THEME, false).apply()
        }
        return ThemeSettings(sharedPreferences.getBoolean(SYSTEM_THEME_ACTIVE, false), false)
    }

    override fun updateThemeAppSetting(settings: ThemeSettings) {
        sharedPreferences.edit().putBoolean(APP_DARK_THEME, settings.isDarkTheme).apply()
    }

    override fun updateThemeSystemSetting(settings: ThemeSettings) {
        sharedPreferences.edit().putBoolean(SYSTEM_THEME_ACTIVE, settings.isActive).apply()
        if (settings.isActive) {
            sharedPreferences.edit().putBoolean(APP_DARK_THEME, false).apply()
        }
    }
}
