package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.data.db.dao.TrackDao
import com.practicum.playlistmaker.data.model.TrackEntity

@Database(version = 2, entities = [TrackEntity::class])
abstract class LikedTracksDatabase: RoomDatabase(){
    abstract fun trackDao(): TrackDao
}
