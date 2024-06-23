package com.yawki.common.data.mapper

import com.yawki.common.data.datasource.local.database.models.MonkEntity
import com.yawki.common.domain.models.monk.Monk
import javax.inject.Inject

class MonkEntityMapper @Inject constructor() : EntityMapper<Monk, MonkEntity> {
    override fun mapToDomain(entityModel: MonkEntity): Monk {
        return Monk(
            id = entityModel.id,
            name = entityModel.name,
            imageUrl = entityModel.imageUrl
        )
    }

    override fun mapToEntity(model: Monk): MonkEntity {
        return MonkEntity(
            id = model.id,
            name = model.name,
            imageUrl = model.imageUrl
        )
    }
}

