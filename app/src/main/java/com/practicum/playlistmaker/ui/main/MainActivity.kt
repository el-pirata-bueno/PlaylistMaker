package com.practicum.playlistmaker.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.ui.medialibrary.activity.MediaActivity
import com.practicum.playlistmaker.ui.models.NavigationRouter
import com.practicum.playlistmaker.ui.search.activity.SearchActivity
import com.practicum.playlistmaker.ui.settings.activity.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var router: NavigationRouter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        router = NavigationRouter(this)

        binding.openSearchScreenButton.setOnClickListener {
            router.openActivity<SearchActivity>(this)
        }

        binding.openMediaScreenButton.setOnClickListener {
            router.openActivity<MediaActivity>(this)
        }

        binding.openSettingsScreenButton.setOnClickListener {
            router.openActivity<SettingsActivity>(this)
        }
    }
}
