package com.yawki.common.data.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yawki.common.data.datasource.local.database.dao.MonkDao
import com.yawki.common.data.datasource.local.database.dao.SongDao
import com.yawki.common.data.datasource.local.database.models.MonkEntity
import com.yawki.common.data.datasource.local.database.models.SongEntity

@Database(
    entities = [
        SongEntity::class,
        MonkEntity::class
    ], version = 3, exportSchema = false
)
abstract class YawkiDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun monkDao(): MonkDao
}