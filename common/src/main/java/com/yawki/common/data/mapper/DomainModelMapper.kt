package com.yawki.common.data.mapper

interface DomainModelMapper<DtoModel, DomainModel> {
    fun mapToDomain(dtoModel: DtoModel): DomainModel

    fun mapToDto(domainModel: DomainModel): DtoModel
}