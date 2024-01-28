package com.practicum.playlistmaker.ui.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMediaBinding

class MediaFragment: Fragment()  {
    private var _binding: FragmentMediaBinding? = null
    private var tabMediator: TabLayoutMediator? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = MediaPagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.liked_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator?.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator?.detach()
    }

    companion object {
        fun newInstance() = MediaFragment().apply {
        }
    }
}