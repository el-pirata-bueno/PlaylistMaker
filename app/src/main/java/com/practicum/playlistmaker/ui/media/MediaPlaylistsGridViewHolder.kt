package com.practicum.playlistmaker.ui.media

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistsViewGridBinding
import com.practicum.playlistmaker.domain.model.Playlist

class MediaPlaylistsGridViewHolder(private val binding: PlaylistsViewGridBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) {
        binding.playlistName.text = model.name
        binding.tracksNumber.text = model.numTracks.toString()
        binding.tracks.text = getDeclination(model.numTracks)

/*
        if (model.imageLink != null) {
            binding.playlistImage.setImageURI(Uri.parse(model.imageLink))
        }
        else {
            binding.playlistImage.setImageResource(R.drawable.cover_placeholder_big)
        }

 */

        val roundedCorner = binding.playlistImage.context.resources.getDimensionPixelSize(R.dimen.player_cover_rounded_corners)
        val link = model.imageLink ?: ""

        Glide.with(itemView)
            .load(link)
            .placeholder(R.drawable.cover_placeholder_big)
            .centerCrop()
            .transform(RoundedCorners(roundedCorner))
            .into(binding.playlistImage)

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

