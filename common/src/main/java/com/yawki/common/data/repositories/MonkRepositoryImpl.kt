package com.yawki.common.data.repositories

import com.google.firebase.firestore.toObjects
import com.yawki.common.data.mapper.DomainModelMapper
import com.yawki.common.data.models.MonkDto
import com.yawki.common.data.datasource.remote.MonkRemoteDataSource
import com.yawki.common.domain.SafeResult
import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.repositories.MonkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MonkRepositoryImpl @Inject constructor(
    private val monkRemoteDataSource: MonkRemoteDataSource,
    private val domainMapper: DomainModelMapper<MonkDto, Monk>
): MonkRepository {
    override fun getMonkList(): Flow<SafeResult<List<Monk>>> = flow {
        val monks = monkRemoteDataSource.getAllMonk().await().toObjects<MonkDto>()

        if (monks.isNotEmpty()) {
            emit(SafeResult.Success(monks.map { domainMapper.mapToDomain(it)  }))
        }
    }
}