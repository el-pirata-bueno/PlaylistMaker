package com.practicum.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.domain.settings.model.ThemeSettings
import com.practicum.playlistmaker.domain.sharing.model.EmailData
import com.practicum.playlistmaker.ui.models.NavigationRouter
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var router: NavigationRouter
    private lateinit var systemTheme: ThemeSettings
    private lateinit var appTheme: ThemeSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        router = NavigationRouter(this)

        viewModel = ViewModelProvider(this, SettingsViewModel.getViewModelFactory())[SettingsViewModel::class.java]

        binding.arrowBackButton.setOnClickListener {
            router.goBack()
        }

        binding.themeSystemSwitcher.isChecked = viewModel.getThemeSystemSettings()
        if (binding.themeSystemSwitcher.isChecked) {
            binding.themeAppSwitcher.isChecked = false
            binding.themeAppSwitcher.isClickable = false
        }
        else {
            binding.themeAppSwitcher.isChecked = viewModel.getThemeAppSettings()
        }


        /*
        viewModel.getSystemActiveThemeLiveData().observe(this) {isDarkTheme ->
            binding.themeSystemSwitcher.isChecked = !isDarkTheme
            if (binding.themeSystemSwitcher.isChecked) {
                binding.themeAppSwitcher.isClickable = false
            }
        }

        viewModel.getAppDarkThemeLiveData().observe(this) {isActive ->
            if (binding.themeSystemSwitcher.isChecked == false) {
                binding.themeAppSwitcher.isChecked = isActive
            }
        }
        */

        initListeners()
    }

    private fun initListeners() {
        binding.themeSystemSwitcher.setOnCheckedChangeListener { switcher, checked ->
            systemTheme = ThemeSettings(checked, (applicationContext as App).isSystemDarkTheme())
            (applicationContext as App).switchTheme(systemTheme.isDarkTheme)
            viewModel.updateThemeSystemSettings(systemTheme)
            if (checked) {
                binding.themeAppSwitcher.isChecked = false
                binding.themeAppSwitcher.isClickable = false
            }
            else {
                binding.themeAppSwitcher.isClickable = true
            }
        }

        binding.themeAppSwitcher.setOnCheckedChangeListener { switcher, checked ->
            val appTheme = ThemeSettings(true, checked)
            (applicationContext as App).switchTheme(appTheme.isDarkTheme)
            viewModel.updateThemeAppSettings(appTheme)
        }

        binding.shareButton.setOnClickListener {
            val shareUrl = getString(R.string.share_button_link)
            val typeText = getString(R.string.input_text_type)
            viewModel.shareApp(shareUrl, typeText)
        }

        binding.helpButton.setOnClickListener {
            val email = EmailData(
                getString(R.string.mailto),
                getString(R.string.email_address),
                getString(R.string.email_subject),
                getString(R.string.email_text)
            )
            viewModel.openSupport(email)
        }

        binding.agreementButton.setOnClickListener {
            val termsUrl = getString(R.string.agreement_link)
            viewModel.openTerms(termsUrl)
        }
    }
}