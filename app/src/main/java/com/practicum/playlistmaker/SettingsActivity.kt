package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val arrowBackButton = findViewById<Button>(R.id.arrow_back_settings)
        val shareButton = findViewById<Button>(R.id.share)
        val helpButton = findViewById<Button>(R.id.help)
        val agreementButton = findViewById<Button>(R.id.agreement)
        val themeSwitcher = findViewById<SwitchCompat>(R.id.themeSwitcher)
        val sharedPrefs = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)

        arrowBackButton.setOnClickListener {
            finish()
        }

        themeSwitcher.isChecked = sharedPrefs.getBoolean(APP_DARK_THEME, false)

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType(getString(R.string.input_text_type))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_button_link))
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
        }

        helpButton.setOnClickListener {
            val helpIntent = Intent(Intent.ACTION_SENDTO)
            helpIntent.data = Uri.parse(getString(R.string.mailto))
            helpIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_address)))
            helpIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
            helpIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
            startActivity(helpIntent)

        }

        agreementButton.setOnClickListener {
            val agreementIntent = Intent(Intent.ACTION_VIEW)
            agreementIntent.data = Uri.parse(getString(R.string.agreement_link))
            startActivity(agreementIntent)
        }

    }
}