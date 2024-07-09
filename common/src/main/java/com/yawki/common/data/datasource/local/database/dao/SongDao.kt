package com.yawki.common.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yawki.common.data.datasource.local.database.models.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Query("SELECT * FROM songs ORDER BY songs.id ASC")
    fun getAll(): Flow<List<SongEntity>>

    @Query("SELECT * FROM songs WHERE songs.monk_id = :monkId ORDER BY songs.id ASC")
    fun getSongs(monkId: Int): Flow<List<SongEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongs(songs: List<SongEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(song: SongEntity): Long

    @Query("DELETE FROM songs")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(song: SongEntity)

    @Query("UPDATE songs SET current_position = :currentPosition WHERE id = :songID")
    suspend fun updateCurrentPosition(songID: Int, currentPosition: Long)

    @Query("UPDATE songs SET is_playing=1 WHERE id = :songID")
    suspend fun setCurrentSongAsPlayed(songID: Int)

    @Query("UPDATE songs SET is_playing=0 WHERE 1")
    suspend fun resetAllSongsAsNotPlayed()

    @Query("SELECT * FROM songs WHERE id = :id")
    suspend fun getSong(id: Int): SongEntity
}