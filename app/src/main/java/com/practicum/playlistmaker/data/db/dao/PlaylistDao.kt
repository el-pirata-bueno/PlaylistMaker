package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.practicum.playlistmaker.data.model.PlaylistEntity

@Dao
interface PlaylistDao {

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun createPlaylist(playlistEntity: PlaylistEntity)

    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updatePlaylist(playlistEntity: PlaylistEntity)

    @Delete(entity = PlaylistEntity::class)
    fun deletePlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlists_table")
    fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlists_table WHERE playlistId = :playlistId")
    fun getPlaylistById(playlistId: Int): PlaylistEntity
}