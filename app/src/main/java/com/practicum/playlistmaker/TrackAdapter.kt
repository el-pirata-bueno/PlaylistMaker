package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackAdapter(
    private val tracks: List<Track>
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tracks_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Информация о треке в разработке", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = tracks.size

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val trackNameView: TextView
        private val artistNameView: TextView
        private val trackTimeView: TextView
        private val artworkView: ImageView

        init {
            trackNameView = itemView.findViewById(R.id.trackName)
            artistNameView = itemView.findViewById(R.id.artistName)
            trackTimeView = itemView.findViewById(R.id.trackTime)
            artworkView = itemView.findViewById(R.id.artwork)
        }

        fun bind(model: Track) {
            trackNameView.text = model.trackName
            artistNameView.text = model.artistName
            trackTimeView.text = model.trackTime
            val imageUrl = model.artworkUrl100
            Glide.with(itemView)
                .load(imageUrl)
                .centerCrop()
                .transform(RoundedCorners(10))
                .into(artworkView)
        }

    }

}