package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.*

class TrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.tracks_view, parent, false)) {

    var trackNameView: TextView = itemView.findViewById(R.id.trackName)
    var artistNameView: TextView = itemView.findViewById(R.id.artistName)
    var trackTimeView: TextView = itemView.findViewById(R.id.trackTime)
    var artworkView: ImageView = itemView.findViewById(R.id.artwork)

    fun bind(model: Track) {
        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackTimeView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
        val imageUrl = model.artworkUrl100
        Glide.with(itemView)
            .load(imageUrl)
            .placeholder(R.drawable.cover_placeholder)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(artworkView)
    }

}