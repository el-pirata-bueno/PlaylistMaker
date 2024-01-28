package com.practicum.playlistmaker.ui.media

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistsViewListBinding
import com.practicum.playlistmaker.domain.model.Playlist

class MediaPlaylistsListViewHolder(private val binding: PlaylistsViewListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) {
        binding.playlistName.text = model.name
        binding.tracksNumber.text = itemView.resources.getQuantityString(R.plurals.plurals_track, model.numTracks, model.numTracks)

        if (model.imageLink != null) {
            binding.playlistImage.setImageURI(Uri.parse(model.imageLink))
        }
        else {
            binding.playlistImage.setImageResource(R.drawable.cover_placeholder_big)
        }
    }

    private fun getDeclination (numTracks: Int): String {
        var result = numTracks
        result %= 100
        if (result in 10..20 ) {
            return " треков"
        }
        result %= 10
        return when {
            result == 0 || result > 4 -> " треков"
            result > 1 -> " трека"
            else -> " трек"
        }
    }
}

