package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.model.TrackInPlaylistEntity

@Dao
interface TrackInPlaylistDao {

    @Insert(entity = TrackInPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertTrackInPlaylist (trackInPlaylistEntity: TrackInPlaylistEntity)

    @Query("DELETE FROM tracks_in_playlists_table WHERE trackId = :trackId")
    fun deleteTrackFromPlaylist (trackId: Long)

    @Query("SELECT * FROM tracks_in_playlists_table")
    fun getTracksInPlaylists(): List<TrackInPlaylistEntity>

    @Query("SELECT * FROM tracks_in_playlists_table WHERE trackId = :trackId")
    fun getTrackInPlaylistById(trackId: Long): TrackInPlaylistEntity
}