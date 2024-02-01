package com.practicum.playlistmaker.ui.media.playlists

import android.net.Uri
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.presentation.media.playlists.MediaEditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MediaEditPlaylistFragment : MediaNewPlaylistFragment()  {

    override val viewModel: MediaEditPlaylistViewModel by viewModel {
        parametersOf(
            requireArguments().getInt(ARGS_PLAYLIST_ID)
        )
    }
    override fun showEditPlaylist(playlist: Playlist?) {
        currentPlaylist = playlist
        if (currentPlaylist != null) {
            if (currentPlaylist!!.imageLink != null) {
                binding.playlistCover.setImageURI(Uri.parse(currentPlaylist!!.imageLink))
            }
            else {
                binding.playlistCover.setImageResource(R.drawable.cover_placeholder_big)
            }
            binding.inputTitleEditText.setText(currentPlaylist!!.name)

            if (binding.inputDescriptionEditText.text != null){
                binding.inputDescriptionEditText.setText(currentPlaylist!!.description)
            }

            binding.titlePlaylist.text = getString(R.string.edit)
            binding.playlistButton.text = getString(R.string.save)
        }
    }

    override fun initButtonListener() {
        binding.playlistButton.setOnClickListener {
            if (coverUri != null) {
                coverFilePath = saveImageToPrivateStorage(
                    coverUri!!,
                    binding.inputTitleEditText.text.toString()
                )
            }
            viewModel.updatePlaylist(
                binding.inputTitleEditText.text.toString(),
                binding.inputDescriptionEditText.text.toString(),
                coverFilePath
            )

            findNavController().navigateUp()
        }
    }

    companion object {
        private const val ARGS_PLAYLIST_ID = "playlistId"
        fun createArgs(playlistId: Int?): Bundle? =
            bundleOf(
                ARGS_PLAYLIST_ID to playlistId
            )
    }
}