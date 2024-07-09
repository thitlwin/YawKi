package com.yawki.common.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yawki.common.data.datasource.local.database.models.FolderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    @Query("SELECT * FROM folders ORDER BY folders.id ASC")
    fun getAll(): Flow<List<FolderEntity>>

    @Query("SELECT * FROM folders ORDER BY folders.monk_id = :monkId ASC")
    fun getAllByMonkId(monkId: Int): Flow<List<FolderEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFolders(songs: List<FolderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(song: FolderEntity): Long

    @Query("DELETE FROM folders")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(song: FolderEntity)

    @Query("SELECT * FROM folders WHERE id = :folderId")
    suspend fun getFolderById(folderId: Int): FolderEntity
}