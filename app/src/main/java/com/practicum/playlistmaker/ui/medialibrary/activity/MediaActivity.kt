package com.practicum.playlistmaker.ui.medialibrary.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.ui.medialibrary.viewmodel.MediaViewModel
import com.practicum.playlistmaker.ui.models.NavigationRouter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaActivity : AppCompatActivity() {
    private val viewModel: MediaViewModel by viewModel()
    private val binding by lazy { ActivityMediaBinding.inflate(layoutInflater) }
    private lateinit var tabMediator: TabLayoutMediator
    private val router: NavigationRouter by lazy { NavigationRouter(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.viewPager.adapter = PagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.liked_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()

        binding.arrowBackButton.setOnClickListener {
            router.goBack()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}