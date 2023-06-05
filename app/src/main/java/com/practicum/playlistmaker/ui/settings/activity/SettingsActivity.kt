package com.practicum.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.domain.settings.model.ThemeSettings
import com.practicum.playlistmaker.domain.sharing.model.EmailData
import com.practicum.playlistmaker.ui.models.NavigationRouter
import com.practicum.playlistmaker.ui.settings.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private val viewModel: SettingsViewModel by viewModel()
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }
    private val router: NavigationRouter by lazy { NavigationRouter(this) }
    private lateinit var systemTheme: ThemeSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.arrowBackButton.setOnClickListener {
            router.goBack()
        }

        binding.themeSystemSwitcher.isChecked = viewModel.getThemeSystemSettings()
        if (binding.themeSystemSwitcher.isChecked) {
            binding.themeAppSwitcher.isChecked = false
            binding.themeAppSwitcher.isClickable = false
        } else {
            binding.themeAppSwitcher.isChecked = viewModel.getThemeAppSettings()
        }

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
            } else {
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