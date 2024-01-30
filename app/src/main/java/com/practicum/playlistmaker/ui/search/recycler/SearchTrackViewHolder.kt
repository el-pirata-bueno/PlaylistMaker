package com.practicum.playlistmaker.ui.search.recycler

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.TracksViewBinding
import com.practicum.playlistmaker.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class SearchTrackViewHolder(private val binding: TracksViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Track) {
        binding.trackName.text = model.trackName
        binding.artistName.text = model.artistName
        if (model.trackTimeMillis != null) {
            binding.trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
        }
        else {
            binding.trackTime.text = "00:00"
        }

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

