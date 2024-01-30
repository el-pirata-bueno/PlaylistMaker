package com.practicum.playlistmaker.ui.media.playlists.recycler

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistsViewListBinding
import com.practicum.playlistmaker.domain.model.Playlist

class MediaPlaylistsListViewHolder(private val binding: PlaylistsViewListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) {
        binding.playlistName.text = model.name
        binding.tracksNumber.text = itemView.resources.getQuantityString(R.plurals.plurals_track, model.numTracks, model.numTracks)

        val roundedCorner = binding.playlistImage.context.resources.getDimensionPixelSize(R.dimen.tracklist_cover_rounded_corners)
        val link = model.imageLink ?: ""

        Glide.with(itemView)
            .load(link)
            .placeholder(R.drawable.cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(roundedCorner))
            .into(binding.playlistImage)
    }
}

