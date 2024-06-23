package com.yawki.common.domain.usecases

import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.repositories.MonkRepository
import javax.inject.Inject

class GetMonkUseCase @Inject constructor(private val monkRepository: MonkRepository) {

    suspend operator fun invoke(monkId: Int): Monk {
        return monkRepository.getMonkById(monkId)
    }
}