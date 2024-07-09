package com.yawki.common.data.mapper

import com.yawki.common.data.datasource.local.database.models.FolderEntity
import com.yawki.common.domain.models.song.Mp3
import javax.inject.Inject

class SongFolderEntityMapper @Inject constructor() : EntityMapper<Mp3.SongFolder, FolderEntity> {
    override fun mapToDomain(entityModel: FolderEntity): Mp3.SongFolder {
        return Mp3.SongFolder(
            id = entityModel.id,
            name = entityModel.name,
            monkId = entityModel.monkId
        )
    }

    override fun mapToEntity(model: Mp3.SongFolder): FolderEntity {
        return FolderEntity(
            id = model.id,
            name = model.name,
            monkId = model.monkId
        )
    }

}