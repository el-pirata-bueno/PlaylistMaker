package com.practicum.playlistmaker

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track  (val trackName: String,
                  val artistName: String,
                  val trackId: Int,
                  val trackTimeMillis: Int,
                  val artworkUrl100: String,
                  val collectionName: String,
                  val releaseDate: String,
                  val primaryGenreName: String,
                  val country: String) : Parcelable

     // trackName - название композиции
     // artistName - имя исполнителя
     // trackId - уникальный id трека
     // trackTime - продолжительность трека
     // artworkUrl100 - ссылка на изображение обложки
     // collectionName - название альбома
     // releaseDate - год релиза трека
     // primaryGenreName - жанр трека
     // country - страна исполнителя
