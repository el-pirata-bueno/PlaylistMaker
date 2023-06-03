package com.practicum.playlistmaker.data.storage

import android.content.SharedPreferences
import com.practicum.playlistmaker.domain.settings.model.ThemeSettings

class SettingsLocalStorage(private val sharedPreferences: SharedPreferences) {
    private companion object {
        const val APP_DARK_THEME = "APP_DARK_THEME"
        const val SYSTEM_THEME_ACTIVE = "SYSTEM_THEME_ACTIVE"
    }

    fun getThemeAppSettings(): ThemeSettings = ThemeSettings(false, sharedPreferences.getBoolean(
        APP_DARK_THEME, false))

    fun getThemeSystemSettings(): ThemeSettings {
        val themeSystemActive =
            ThemeSettings(sharedPreferences.getBoolean(SYSTEM_THEME_ACTIVE, false), false)
        if (themeSystemActive.isActive) {
            sharedPreferences.edit().putBoolean(APP_DARK_THEME, false).apply()
        }
        return ThemeSettings(sharedPreferences.getBoolean(SYSTEM_THEME_ACTIVE, false), false)
    }

    fun updateThemeAppSetting(settings: ThemeSettings) {
        sharedPreferences.edit().putBoolean(APP_DARK_THEME, settings.isDarkTheme).apply()
    }

    fun updateThemeSystemSetting(settings: ThemeSettings) {
        sharedPreferences.edit().putBoolean(SYSTEM_THEME_ACTIVE, settings.isActive).apply()
        if (settings.isActive) {
            sharedPreferences.edit().putBoolean(APP_DARK_THEME, false).apply()
        }
    }
}
