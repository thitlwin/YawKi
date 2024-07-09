package com.yawki.common.data.repositories

import com.google.firebase.firestore.toObjects
import com.yawki.common.data.datasource.local.database.dao.MonkDao
import com.yawki.common.data.datasource.local.database.models.MonkEntity
import com.yawki.common.data.mapper.DomainModelMapper
import com.yawki.common.data.models.MonkDto
import com.yawki.common.data.datasource.remote.MonkRemoteDataSource
import com.yawki.common.data.mapper.EntityMapper
import com.yawki.common.domain.SafeResult
import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.repositories.MonkRepository
import com.yawki.common.utils.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MonkRepositoryImpl @Inject constructor(
    private val monkDao: MonkDao,
    private val monkRemoteDataSource: MonkRemoteDataSource,
    private val domainMapper: DomainModelMapper<MonkDto, Monk>,
    private val entityMapper: EntityMapper<Monk, MonkEntity>,
) : MonkRepository {

//    override fun getMonkList(): Flow<SafeResult<List<Monk>>> = flow {
//        val monks = monkRemoteDataSource.getAllMonk().await().toObjects<MonkDto>()
//
//        if (monks.isNotEmpty()) {
//            emit(SafeResult.Success(monks.map { domainMapper.mapToDomain(it)  }))
//        }
//    }

    override fun getMonkList(): Flow<SafeResult<List<Monk>>> = networkBoundResource(
        query = {
            monkDao.getAll()
                .map { monkEntities -> monkEntities.map { entityMapper.mapToDomain(it) } }
        },
        fetch = {
            val monks = monkRemoteDataSource.getAllMonk().await().toObjects<MonkDto>()
            monks.map { domainMapper.mapToDomain(it) }
        },
        saveFetchedResult = { monks ->
            val monkEntities = monks.map { entityMapper.mapToEntity(it) }
            monkDao.insertMonks(monkEntities)
        }
    )

    override suspend fun getMonkById(monkId: Int): Monk {
        return entityMapper.mapToDomain(monkDao.getMonkById(monkId))
    }

}