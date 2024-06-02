package com.yawki.common.data.mapper

import com.yawki.common.data.models.MonkDto
import com.yawki.common.domain.models.monk.Monk
import javax.inject.Inject

class MonkDomainModelMapper @Inject constructor() : DomainModelMapper<MonkDto, Monk> {
    override fun mapToDomain(dtoModel: MonkDto): Monk {
        return Monk(
            id = dtoModel.id,
            name = dtoModel.name,
            imageUrl = dtoModel.imageUrl
        )
    }

    override fun mapToDto(domainModel: Monk): MonkDto {
        return MonkDto(
            id = domainModel.id,
            name = domainModel.name,
            imageUrl = domainModel.imageUrl
        )
    }

}