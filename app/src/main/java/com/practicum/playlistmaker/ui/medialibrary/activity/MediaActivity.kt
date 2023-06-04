package com.practicum.playlistmaker.ui.medialibrary.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.ui.models.NavigationRouter

class MediaActivity : AppCompatActivity() {
    private lateinit var router: NavigationRouter
    private lateinit var binding: ActivityMediaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        router = NavigationRouter(this)

        binding.arrowBackButton.setOnClickListener {
            router.goBack()
        }
    }
}