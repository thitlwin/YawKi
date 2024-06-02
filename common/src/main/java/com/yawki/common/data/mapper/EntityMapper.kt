package com.yawki.common.data.mapper

interface EntityMapper<DomainModel, EntityModel> {
    fun mapToDomain(entityModel: EntityModel): DomainModel

    fun mapToEntity(model: DomainModel): EntityModel
}