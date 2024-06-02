package com.yawki.common.domain.mapper

interface UiModelMapper<DomainModel, UiModel> {
    fun mapToUi(domainModel: DomainModel): UiModel

    fun mapToDomain(uiModel: UiModel): DomainModel
}