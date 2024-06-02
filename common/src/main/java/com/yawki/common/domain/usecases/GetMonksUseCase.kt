package com.yawki.common.domain.usecases

import com.yawki.common.domain.SafeResult
import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.repositories.MonkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMonksUseCase @Inject constructor(private val monkRepository: MonkRepository) {

    operator fun invoke(): Flow<SafeResult<List<Monk>>> {
        return monkRepository.getMonkList()
    }
}