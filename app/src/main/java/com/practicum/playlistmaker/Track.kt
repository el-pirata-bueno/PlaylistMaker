package com.practicum.playlistmaker

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

data class Track (val trackName: String, val artistName: String, val trackTime: String, val artworkUrl100: String)
     // trackName - название композиции
     // artistName - имя исполнителя
     // trackTime - продолжительность трека
     // artworkUrl100 - ссылка на изображение обложки

