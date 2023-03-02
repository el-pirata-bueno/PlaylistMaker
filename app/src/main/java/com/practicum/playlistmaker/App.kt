package com.practicum.playlistmaker

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

const val APP_SETTINGS = "app_settings"
const val APP_DARK_THEME = "app_dark_theme"
const val SEARCH_HISTORY = "search_history"
const val TRACKLIST_HISTORY = "tracklist_history"


class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefsTheme = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)
        darkTheme = sharedPrefsTheme.getBoolean(APP_DARK_THEME, isDarkMode(this))
        sharedPrefsTheme.edit()
            .putBoolean(APP_DARK_THEME, darkTheme)
            .apply()
        switchTheme(darkTheme)
    }

    fun isDarkMode(context: Context): Boolean {
        val darkModeFlag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}