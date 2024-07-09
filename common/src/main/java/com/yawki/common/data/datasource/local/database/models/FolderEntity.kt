package com.yawki.common.data.datasource.local.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yawki.common.domain.models.song.ItemType

@Entity(tableName = "folders")
data class FolderEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "monk_id") val monkId: Int
)