package com.practicum.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.presentation.models.TrackUi

class TrackAdapter() : RecyclerView.Adapter<TrackViewHolder>() {

    var itemClickListener: ((TrackUi) -> Unit)? = null
    val tracks = ArrayList<TrackUi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(track)
        }
    }

    override fun getItemCount() = tracks.size

}