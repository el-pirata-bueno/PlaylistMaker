package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val arrowBackButton = findViewById<Button>(R.id.arrow_back)

        arrowBackButton.setOnClickListener {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }

        val shareButton = findViewById<Button>(R.id.share)
        val helpButton = findViewById<Button>(R.id.help)
        val agreementButton = findViewById<Button>(R.id.agreement)

        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_button_link))
            startActivity(Intent.createChooser(shareIntent, "Поделиться приложением"))
        }

        helpButton.setOnClickListener {
            val helpIntent = Intent(Intent.ACTION_SENDTO)
            helpIntent.data = Uri.parse("mailto:")
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