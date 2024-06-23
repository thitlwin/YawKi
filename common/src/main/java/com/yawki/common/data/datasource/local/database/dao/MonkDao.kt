package com.yawki.common.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yawki.common.data.datasource.local.database.models.MonkEntity
import com.yawki.common.domain.models.monk.Monk
import kotlinx.coroutines.flow.Flow

@Dao
interface MonkDao {
    @Query("SELECT * FROM monks ORDER BY monks.id ASC")
    fun getAll(): Flow<List<MonkEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMonks(songs: List<MonkEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(song: MonkEntity): Long

    @Query("DELETE FROM monks")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(song: MonkEntity)

    @Query("SELECT * FROM monks WHERE id = :monkId")
    suspend fun getMonkById(monkId: Int): MonkEntity
}