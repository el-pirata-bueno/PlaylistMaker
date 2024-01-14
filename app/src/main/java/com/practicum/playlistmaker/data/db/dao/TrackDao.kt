package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.model.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLikedTrack(trackEntity: TrackEntity)

    @Delete(entity = TrackEntity::class)
    fun deleteLikedTrack(trackEntity: TrackEntity)

    @Query("SELECT * FROM track_table ORDER BY createdAt DESC")
    fun getLikedTracks(): List<TrackEntity>

    // НУЖЕН??
    @Query("SELECT * FROM track_table WHERE trackId = :id")
    fun getTrackById(id: Long): TrackEntity

    @Query("SELECT trackId FROM track_table")
    fun getTrackIds(): List<Long>
}