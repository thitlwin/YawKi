package com.yawki.common.data.repositories

import com.google.firebase.firestore.toObjects
import com.yawki.common.data.datasource.remote.SongRemoteDataSource
import com.yawki.common.data.mapper.DomainModelMapper
import com.yawki.common.data.models.SongDto
import com.yawki.common.domain.SafeResult
import com.yawki.common.domain.getSuccessOrNull
import com.yawki.common.domain.models.song.Song
import com.yawki.common.domain.repositories.SongRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    private val songRemoteDataSource: SongRemoteDataSource,
    private val domainMapper: DomainModelMapper<SongDto, Song>
): SongRepository {
    override fun getSongList(monkId: Int): Flow<SafeResult<List<Song>>> = flow {
        val songs = songRemoteDataSource.getSongByMonk(monkId).await().toObjects<SongDto>()
        if (songs.isNotEmpty()) {
            emit(SafeResult.Success(songs.map { domainMapper.mapToDomain(it) }))
        }
    }
}