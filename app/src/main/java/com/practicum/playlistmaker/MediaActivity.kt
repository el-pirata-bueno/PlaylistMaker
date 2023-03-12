package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MediaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val arrowBackButton = findViewById<Button>(R.id.arrow_back_media)

        arrowBackButton.setOnClickListener {
            finish()
        }
    }
}