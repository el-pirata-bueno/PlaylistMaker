package com.practicum.playlistmaker.ui.medialibrary.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentLikedTracksBinding
import com.practicum.playlistmaker.ui.medialibrary.viewmodel.TrackCollectionsVIewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class LikedTracksFragment: Fragment() {

    companion object {
        fun newInstance() = LikedTracksFragment().apply { }
    }

    val hostViewModel by activityViewModel<TrackCollectionsVIewModel>()

    private var _binding: FragmentLikedTracksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikedTracksBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}