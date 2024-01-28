package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.practicum.playlistmaker.data.model.TrackInPlaylistEntity

@Dao
interface TrackInPlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTrackInPlaylist (trackInPlaylistEntity: TrackInPlaylistEntity)

    @Delete(entity = TrackInPlaylistEntity::class)
    fun deleteTrackFromPlaylist (trackInPlaylistEntity: TrackInPlaylistEntity)

}