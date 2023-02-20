package com.practicum.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter () : RecyclerView.Adapter<TrackViewHolder> () {

    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks.get(position))
        //holder.itemView.setOnClickListener {
        //    Toast.makeText(holder.itemView.context, "Информация о треке в разработке", Toast.LENGTH_SHORT).show()
        //}
    }

    override fun getItemCount() = tracks.size

}