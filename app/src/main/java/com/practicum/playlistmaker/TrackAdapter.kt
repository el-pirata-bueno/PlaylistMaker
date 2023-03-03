package com.practicum.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter () : RecyclerView.Adapter<TrackViewHolder> () {

    var itemClickListener: ((Track) -> Unit)? = null
    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks.get(position)
        holder.bind(track)
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(track)
            //Toast.makeText(holder.itemView.context, "Информация о треке ${holder.artistNameView.text} - ${holder.trackNameView.text} в разработке", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = tracks.size

}