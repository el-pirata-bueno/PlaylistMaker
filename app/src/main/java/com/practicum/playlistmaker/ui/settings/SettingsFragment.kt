package com.practicum.playlistmaker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.domain.settings.model.ThemeSettings
import com.practicum.playlistmaker.domain.sharing.model.EmailData
import com.practicum.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModel()
    private lateinit var systemTheme: ThemeSettings

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            systemTheme = ThemeSettings(checked, (getActivity()?.getApplicationContext() as App).isSystemDarkTheme())
            (getActivity()?.getApplicationContext() as App).switchTheme(systemTheme.isDarkTheme)
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
            (getActivity()?.getApplicationContext() as App).switchTheme(appTheme.isDarkTheme)
            viewModel.updateThemeAppSettings(appTheme)
        }

        binding.shareButton.setOnClickListener {
            val shareUrl = getString(R.string.share_button_link)
            val typeText = getString(R.string.input_text_type)
            viewModel.shareApp(shareUrl, typeText)
        }

        binding.helpButton.setOnClickListener {
            val email = EmailData(
                getString(R.string.input_text_type),
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

    companion object {
        fun newInstance() = SettingsFragment().apply {
        }

    }

}