package com.practicum.playlistmaker.ui.media

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.ui.media.favorites.MediaLikedTracksFragment
import com.practicum.playlistmaker.ui.media.playlists.MediaPlaylistsFragment

class MediaPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) MediaLikedTracksFragment.newInstance() else MediaPlaylistsFragment.newInstance()
        }
}