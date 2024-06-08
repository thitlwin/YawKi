package com.yawki.common.data.mapper

import com.yawki.common.data.models.SongDto
import com.yawki.common.domain.models.song.Song
import javax.inject.Inject

class SongDomainModelMapper @Inject constructor() : DomainModelMapper<SongDto, Song> {
    override fun mapToDomain(dtoModel: SongDto): Song {
        return Song(
            id = dtoModel.id.toString(),
            name = dtoModel.name,
            serialNo = dtoModel.serialNo,
            monk = dtoModel.monk,
            fileUrl = dtoModel.fileUrl,
            artworkUri = dtoModel.artworkUri)
    }

    override fun mapToDto(domainModel: Song): SongDto {
        return SongDto(
            id = domainModel.id.toInt(),
            name = domainModel.name,
            serialNo = domainModel.serialNo,
            monkId = 0,
            fileUrl = domainModel.fileUrl,
            artworkUri = domainModel.artworkUri
        )
    }
}

