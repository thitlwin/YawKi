package com.yawki.common.data.repositories

import com.google.firebase.firestore.toObjects
import com.yawki.common.data.datasource.local.database.dao.SongDao
import com.yawki.common.data.datasource.local.database.models.SongEntity
import com.yawki.common.data.datasource.remote.SongRemoteDataSource
import com.yawki.common.data.mapper.DomainModelMapper
import com.yawki.common.data.mapper.EntityMapper
import com.yawki.common.data.models.SongDto
import com.yawki.common.domain.SafeResult
import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.models.song.Song
import com.yawki.common.domain.repositories.SongRepository
import com.yawki.common.utils.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    private val songDao: SongDao,
    private val songRemoteDataSource: SongRemoteDataSource,
    private val domainMapper: DomainModelMapper<SongDto, Song>,
    private val entityMapper: EntityMapper<Song, SongEntity>,
) : SongRepository {

    override fun getSongList(
        monk: Monk,
        forceRefresh: Boolean,
    ): Flow<SafeResult<List<Song>>> = networkBoundResource(
        query = {
            songDao.getSongs(monk.id)
                .map { songEntities -> songEntities.map { entityMapper.mapToDomain(it).copy(monkName = monk.name) } }
        },
        fetch = {
            val songs = songRemoteDataSource.getSongByMonk(monk.id).await().toObjects<SongDto>()
            songs.map { domainMapper.mapToDomain(it) }
        },
        saveFetchResult = { songs ->
            val songEntities = songs.map { entityMapper.mapToEntity(it).copy(monkId = monk.id) }
            songDao.insertSongs(songEntities)
        })

    override suspend fun updateSong(song: Song, monk: Monk): Long {
        val songEntity = entityMapper.mapToEntity(song).copy(monkId = monk.id)
        return songDao.upsert(songEntity)
    }
}