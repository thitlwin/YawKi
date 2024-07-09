package com.yawki.common.data.mapper

import com.yawki.common.data.models.Mp3Dto
import com.yawki.common.domain.models.song.Mp3
import javax.inject.Inject

class SongFolderDomainModelMapper @Inject constructor() : DomainModelMapper<Mp3Dto.SongFolderDto, Mp3.SongFolder> {
    override fun mapToDomain(dtoModel: Mp3Dto.SongFolderDto): Mp3.SongFolder {
        return Mp3.SongFolder(
            id = dtoModel.id,
            name = dtoModel.name,
            monkId = dtoModel.monkId
        )
    }

    override fun mapToDto(domainModel: Mp3.SongFolder): Mp3Dto.SongFolderDto {
        return Mp3Dto.SongFolderDto(
            id = domainModel.id,
            name = domainModel.name,
            monkId = domainModel.monkId
        )
    }
}

