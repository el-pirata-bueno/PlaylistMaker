package com.practicum.playlistmaker.ui.search.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.TracksViewBinding
import com.practicum.playlistmaker.domain.model.Track

class SearchTrackAdapter : RecyclerView.Adapter<SearchTrackViewHolder>() {

    var itemClickListener: ((Track) -> Unit)? = null
    var itemLongClickListener: ((Track) -> Unit)? = null
    val tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return SearchTrackViewHolder(
            TracksViewBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchTrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)

        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(track)
        }
        holder.itemView.setOnLongClickListener {
            itemLongClickListener?.invoke(track)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount() = tracks.size

}

