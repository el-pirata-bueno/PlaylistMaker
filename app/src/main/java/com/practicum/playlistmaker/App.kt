package com.practicum.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModelModule
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.model.ThemeSettings
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class App : Application(), KoinComponent {

    private lateinit var themeAppSettings: ThemeSettings
    private lateinit var themeSystemSettings: ThemeSettings

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

        themeAppSettings = getKoin().get<SettingsInteractor>().getThemeAppSettings()
        themeSystemSettings = getKoin().get<SettingsInteractor>().getThemeSystemSettings()

        if (themeSystemSettings.isActive) {
            if (isSystemDarkTheme()) {
                themeSystemSettings.isDarkTheme = true
            }
            switchTheme(themeSystemSettings.isDarkTheme)
        } else {
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