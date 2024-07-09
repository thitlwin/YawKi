package com.yawki.common.data.repositories

import com.google.firebase.firestore.toObjects
import com.yawki.common.data.datasource.local.database.dao.FolderDao
import com.yawki.common.data.datasource.local.database.dao.SongDao
import com.yawki.common.data.datasource.local.database.models.FolderEntity
import com.yawki.common.data.datasource.local.database.models.SongEntity
import com.yawki.common.data.datasource.remote.SongRemoteDataSource
import com.yawki.common.data.mapper.DomainModelMapper
import com.yawki.common.data.mapper.EntityMapper
import com.yawki.common.data.models.Mp3Dto
import com.yawki.common.domain.SafeResult
import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.models.song.Mp3
import com.yawki.common.domain.repositories.SongRepository
import com.yawki.common.utils.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    private val folderDao: FolderDao,
    private val songDao: SongDao,
    private val songRemoteDataSource: SongRemoteDataSource,
    private val songDomainMapper: DomainModelMapper<Mp3Dto.SongDto, Mp3.Song>,
    private val songEntityMapper: EntityMapper<Mp3.Song, SongEntity>,
    private val songFolderDomainMapper: DomainModelMapper<Mp3Dto.SongFolderDto, Mp3.SongFolder>,
    private val songFolderEntityMapper: EntityMapper<Mp3.SongFolder, FolderEntity>,
) : SongRepository {

    override fun getSongList(
        monk: Monk,
        forceRefresh: Boolean,
    ): Flow<SafeResult<List<Mp3>>> = networkBoundResource(
        query = {
            val songFolderFlow = folderDao.getAllByMonkId(monk.id)
                .map { folderEntities -> folderEntities.map { songFolderEntityMapper.mapToDomain(it) }}

            val songFlow = songDao.getSongs(monk.id)
                .map { songEntities ->
                    songEntities.map {
                        songEntityMapper.mapToDomain(it).copy(monkName = monk.name)
                    }
                }
            songFolderFlow.combine(songFlow) { folders, songs ->
                folders + songs
            }
        },
        fetch = {
            val songDtos =
                songRemoteDataSource.getSongByMonk(monk.id).await().toObjects<Mp3Dto.SongDto>()
            val songs = songDtos.map { songDomainMapper.mapToDomain(it) }
            val songSongFolderDtos =
                songRemoteDataSource.getSongFolderByMonk(monk.id).await().toObjects<Mp3Dto.SongFolderDto>()
            val songFolders = songSongFolderDtos.map { songFolderDomainMapper.mapToDomain(it) }
            songFolders + songs
        },
        saveFetchedResult = { songs ->

            songs.filterIsInstance<Mp3.SongFolder>().let { songFolders ->
                val songFolderEntities =
                    songFolders.map { songFolderEntityMapper.mapToEntity(it).copy(monkId = monk.id) }
                folderDao.insertFolders(songFolderEntities)
            }
            songs.filterIsInstance<Mp3.Song>().let { songList ->
                val songEntities =
                    songList.map { songEntityMapper.mapToEntity(it).copy(monkId = monk.id) }
                songDao.insertSongs(songEntities)
            }
        })

    override suspend fun updateSong(song: Mp3.Song, monk: Monk): Long {
        val songEntity = songEntityMapper.mapToEntity(song).copy(monkId = monk.id)
        return songDao.upsert(songEntity)
    }

    override suspend fun updateCurrentPosition(songID: Int, currentPosition: Long) {
        songDao.updateCurrentPosition(songID, currentPosition)
    }
}