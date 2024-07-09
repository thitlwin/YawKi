package com.yawki.common.data.datasource.local.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yawki.common.domain.models.song.ItemType

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "serial_no") val serialNo: Int,
    @ColumnInfo(name = "monk_id") val monkId: Int,
    @ColumnInfo(name = "file_url") val fileUrl: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false,
    @ColumnInfo(name = "artwork_uri") val artworkUri: String,
    @ColumnInfo(name = "is_playing") var isPlaying: Boolean = false,
    @ColumnInfo(name = "current_position") val currentPosition: Long = 0L,
    @ColumnInfo(name = "item_type") val itemType: ItemType = ItemType.SONG,
)