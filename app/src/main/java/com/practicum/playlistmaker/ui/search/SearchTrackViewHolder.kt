package com.practicum.playlistmaker.ui.search

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.TracksViewBinding
import com.practicum.playlistmaker.domain.model.Track

class SearchTrackViewHolder(private val binding: TracksViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Track) {
        binding.trackName.text = model.trackName
        binding.artistName.text = model.artistName
        binding.trackTime.text = model.trackTime

        val imageUrl = model.artworkUrl100

        val roundedCorner = binding.artwork.context.resources.getDimensionPixelSize(R.dimen.tracklist_cover_rounded_corners)

        Glide.with(itemView)
            .load(imageUrl)
            .placeholder(R.drawable.cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(roundedCorner))
            .into(binding.artwork)
    }

}

