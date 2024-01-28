package com.practicum.playlistmaker.ui.media

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.PlaylistsViewListBinding
import com.practicum.playlistmaker.domain.model.Playlist

class MediaPlaylistsListAdapter : RecyclerView.Adapter<MediaPlaylistsListViewHolder>() {

    var itemClickListener: ((Playlist) -> Unit)? = null
    val playlists = ArrayList<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaPlaylistsListViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return MediaPlaylistsListViewHolder(
            PlaylistsViewListBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MediaPlaylistsListViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(playlist)
        }
    }

    override fun getItemCount() = playlists.size

}