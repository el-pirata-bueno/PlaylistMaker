package com.practicum.playlistmaker.ui.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMediaBinding
import com.practicum.playlistmaker.presentation.media.MediaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFragment: Fragment()  {
    private lateinit var binding: FragmentMediaBinding
    private val viewModel: MediaViewModel by viewModel()
    private lateinit var tabMediator: TabLayoutMediator

    companion object {
        fun newInstance() = run { }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = PagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.liked_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}