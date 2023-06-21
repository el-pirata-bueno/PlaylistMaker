package com.practicum.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.TracksViewBinding
import com.practicum.playlistmaker.ui.models.TrackUi

class TrackAdapter() : RecyclerView.Adapter<TrackViewHolder>() {

    var itemClickListener: ((TrackUi) -> Unit)? = null
    val tracks = ArrayList<TrackUi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(
            TracksViewBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(track)
        }
    }

    override fun getItemCount() = tracks.size

}