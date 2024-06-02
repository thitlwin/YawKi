package com.yawki.common.domain.repositories

import com.yawki.common.domain.SafeResult
import com.yawki.common.domain.models.monk.Monk
import kotlinx.coroutines.flow.Flow

interface MonkRepository {
    fun getMonkList(): Flow<SafeResult<List<Monk>>>

}