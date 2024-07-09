package com.yawki.common.data.mapper

import com.yawki.common.data.datasource.local.database.models.SongEntity
import com.yawki.common.domain.models.song.ItemType
import com.yawki.common.domain.models.song.Mp3
import javax.inject.Inject

class SongEntityMapper @Inject constructor() : EntityMapper<Mp3.Song, SongEntity> {

    override fun mapToDomain(entityModel: SongEntity): Mp3.Song {
        return Mp3.Song(
            id = entityModel.id,
            name = entityModel.name,
            serialNo = entityModel.serialNo,
            monkName = "-",
            fileUrl = entityModel.fileUrl,
            artworkUri = entityModel.artworkUri,
            isFavorite = entityModel.isFavorite,
            itemType = ItemType.SONG,
            isPlaying = entityModel.isPlaying,
            currentPosition = entityModel.currentPosition
        )
    }

    override fun mapToEntity(model: Mp3.Song): SongEntity {
        return SongEntity(
            id = model.id,
            name = model.name,
            serialNo = model.serialNo,
            monkId = -1,
            fileUrl = model.fileUrl,
            artworkUri = model.artworkUri,
            isFavorite = model.isFavorite,
            itemType = ItemType.fromChar(model.itemType.value),
            isPlaying = model.isPlaying,
            currentPosition = model.currentPosition
        )
    }
}

