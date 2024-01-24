package com.practicum.playlistmaker.ui.media

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.PlaylistsViewGridBinding
import com.practicum.playlistmaker.domain.model.Playlist

class MediaPlaylistsGridAdapter : RecyclerView.Adapter<MediaPlaylistsGridViewHolder>() {

    var itemClickListener: ((Playlist) -> Unit)? = null
    val playlists = ArrayList<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaPlaylistsGridViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return MediaPlaylistsGridViewHolder(
            PlaylistsViewGridBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MediaPlaylistsGridViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(playlist)
        }
    }

    override fun getItemCount() = playlists.size

}