package com.practicum.playlistmaker.ui.settings.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.model.ThemeSettings
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.model.EmailData
import com.practicum.playlistmaker.util.Creator

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor) : ViewModel() {

    /*
    private var systemActiveThemeLiveData = MutableLiveData<Boolean>()
    fun getSystemActiveThemeLiveData(): LiveData<Boolean> = systemActiveThemeLiveData

    private var appDarkThemeLiveData = MutableLiveData<Boolean>()
    fun getAppDarkThemeLiveData(): LiveData<Boolean> = appDarkThemeLiveData

     */

    fun getThemeSystemSettings(): Boolean {
        return settingsInteractor.getThemeSystemSettings().isActive
    }

    fun getThemeAppSettings(): Boolean {
        return settingsInteractor.getThemeAppSettings().isDarkTheme
    }

    fun updateThemeAppSettings(settings: ThemeSettings) {
        settingsInteractor.updateThemeAppSetting(settings)
    }

    fun updateThemeSystemSettings(settings: ThemeSettings) {
        settingsInteractor.updateThemeSystemSetting(settings)
    }

    fun shareApp(shareUrl: String, typeText: String) {
        sharingInteractor.shareApp(shareUrl, typeText)
    }

    fun openSupport(email: EmailData) {
        sharingInteractor.openSupport(email)
    }

    fun openTerms(termsUrl: String) {
        sharingInteractor.openTerms(termsUrl)
    }

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as App
                SettingsViewModel(
                    settingsInteractor = Creator.provideSettingsInteractor(application),
                    sharingInteractor = Creator.provideSharingInteractor(application)
                )
            }
        }
    }
}
