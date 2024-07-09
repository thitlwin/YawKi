package com.yawki.common.data.mapper

import com.yawki.common.data.models.Mp3Dto
import com.yawki.common.domain.models.song.Mp3
import javax.inject.Inject

class SongDomainModelMapper @Inject constructor() : DomainModelMapper<Mp3Dto.SongDto, Mp3.Song> {
    override fun mapToDomain(dtoModel: Mp3Dto.SongDto): Mp3.Song {
        return Mp3.Song(
            id = dtoModel.id,
            name = dtoModel.name,
            serialNo = dtoModel.serialNo,
            monkName = dtoModel.monk,
            fileUrl = dtoModel.fileUrl,
            artworkUri = dtoModel.artworkUri,
        )
    }

    override fun mapToDto(domainModel: Mp3.Song): Mp3Dto.SongDto {
        return Mp3Dto.SongDto(
            id = domainModel.id,
            name = domainModel.name,
            serialNo = domainModel.serialNo,
            monkId = 0,
            fileUrl = domainModel.fileUrl,
            artworkUri = domainModel.artworkUri,
        )
    }
}

