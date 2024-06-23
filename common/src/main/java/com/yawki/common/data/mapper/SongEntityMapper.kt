package com.yawki.common.data.mapper

import com.yawki.common.data.datasource.local.database.models.SongEntity
import com.yawki.common.domain.models.song.Song
import javax.inject.Inject

class SongEntityMapper @Inject constructor() : EntityMapper<Song, SongEntity> {

    override fun mapToDomain(entityModel: SongEntity): Song {
        return Song(
            id = entityModel.id,
            name = entityModel.name,
            serialNo = entityModel.serialNo,
            monkName = "-",
            fileUrl = entityModel.fileUrl,
            artworkUri = entityModel.artworkUri,
            isFavorite = entityModel.isFavorite
        )
    }

    override fun mapToEntity(model: Song): SongEntity {
        return SongEntity(
            id = model.id,
            name = model.name,
            serialNo = model.serialNo,
            monkId = -1,
            fileUrl = model.fileUrl,
            artworkUri = model.artworkUri,
            isFavorite = model.isFavorite
        )
    }
}

