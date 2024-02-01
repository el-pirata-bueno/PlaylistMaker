package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.data.db.dao.TrackDao
import com.practicum.playlistmaker.data.db.dao.TrackInPlaylistDao
import com.practicum.playlistmaker.data.model.PlaylistEntity
import com.practicum.playlistmaker.data.model.TrackEntity
import com.practicum.playlistmaker.data.model.TrackInPlaylistEntity

@Database(version = 5, entities = [TrackEntity::class, PlaylistEntity::class, TrackInPlaylistEntity::class])
abstract class AppDatabase: RoomDatabase(){
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun trackInPlaylistDao(): TrackInPlaylistDao
}
