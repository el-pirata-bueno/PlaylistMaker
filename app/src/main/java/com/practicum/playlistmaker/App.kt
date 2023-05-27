package com.practicum.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.data.settings.SettingsLocalStorage
import com.practicum.playlistmaker.domain.settings.model.ThemeSettings

class App : Application() {

    private lateinit var themeAppSettings: ThemeSettings
    private lateinit var themeSystemSettings: ThemeSettings

    override fun onCreate() {
        super.onCreate()
        val localStorage = SettingsLocalStorage(getSharedPreferences("APP_SETTINGS", MODE_PRIVATE))

        themeAppSettings = localStorage.getThemeAppSettings()
        themeSystemSettings = localStorage.getThemeSystemSettings()

        if (themeSystemSettings.isActive) {
            if (isSystemDarkTheme()) {
                themeSystemSettings.isDarkTheme = true
            }
            switchTheme(themeSystemSettings.isDarkTheme)
        }

        else {
            themeAppSettings.isActive = true
            switchTheme(themeAppSettings.isDarkTheme)
        }
    }

    fun isSystemDarkTheme(): Boolean {
        return (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}