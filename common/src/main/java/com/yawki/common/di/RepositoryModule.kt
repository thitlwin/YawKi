package com.yawki.common.di

import com.google.firebase.firestore.FirebaseFirestore
import com.yawki.common.data.datasource.local.database.dao.FolderDao
import com.yawki.common.data.datasource.local.database.dao.MonkDao
import com.yawki.common.data.datasource.local.database.dao.SongDao
import com.yawki.common.data.datasource.local.database.models.FolderEntity
import com.yawki.common.data.datasource.local.database.models.MonkEntity
import com.yawki.common.data.datasource.local.database.models.SongEntity
import com.yawki.common.data.datasource.remote.MonkRemoteDataSource
import com.yawki.common.data.datasource.remote.SongRemoteDataSource
import com.yawki.common.data.mapper.DomainModelMapper
import com.yawki.common.data.mapper.EntityMapper
import com.yawki.common.data.mapper.SongFolderDomainModelMapper
import com.yawki.common.data.models.MonkDto
import com.yawki.common.data.models.Mp3Dto
import com.yawki.common.data.repositories.AuthRepositoryImpl
import com.yawki.common.data.repositories.MonkRepositoryImpl
import com.yawki.common.data.repositories.SongRepositoryImpl
import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.models.song.Mp3
import com.yawki.common.domain.models.song.Mp3.Song
import com.yawki.common.domain.repositories.AuthRepository
import com.yawki.common.domain.repositories.MonkRepository
import com.yawki.common.domain.repositories.SongRepository
import com.yawki.common.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideFirebaseFirestore() =
        FirebaseFirestore.getInstance()//.collection(Constants.MONK_COLLECTION)

    @Singleton
    @Provides
    fun provideMonkRemoteDataSource(firebaseFirestore: FirebaseFirestore) =
        MonkRemoteDataSource(firebaseFirestore.collection(Constants.MONK_COLLECTION))

    @Provides
    @Singleton
    fun bindMonkRepository(
        monkDao: MonkDao,
        monkRemoteDataSource: MonkRemoteDataSource,
        domainMapper: DomainModelMapper<MonkDto, Monk>,
        entityMapper: EntityMapper<Monk, MonkEntity>
    ): MonkRepository = MonkRepositoryImpl(
        monkDao,
        monkRemoteDataSource,
        domainMapper,
        entityMapper
    )

    @Singleton
    @Provides
    fun provideSongRemoteDataSource(firebaseFirestore: FirebaseFirestore) =
        SongRemoteDataSource(firebaseFirestore.collection(Constants.SONG_COLLECTION))


    @Provides
    @Singleton
    fun bindSongRepository(
        folderDao: FolderDao,
        songDao: SongDao,
        songRemoteDataSource: SongRemoteDataSource,
        songDomainMapper: DomainModelMapper<Mp3Dto.SongDto, Song>,
        songEntityMapper: EntityMapper<Song, SongEntity>,
        songFolderDomainModelMapper: SongFolderDomainModelMapper,
        folderEntityMapper: EntityMapper<Mp3.SongFolder, FolderEntity>
    ): SongRepository = SongRepositoryImpl(
        folderDao = folderDao,
        songDao = songDao,
        songRemoteDataSource = songRemoteDataSource,
        songDomainMapper = songDomainMapper,
        songEntityMapper = songEntityMapper,
        songFolderEntityMapper = folderEntityMapper,
        songFolderDomainMapper = songFolderDomainModelMapper
    )


//    @Singleton
//    @Provides
//    fun provideMusicRepository(
//        musicRemoteDatabase: MusicRemoteDatabase,
//    ): MusicRepository =
//        MusicRepositoryImpl(musicRemoteDatabase)

    @Singleton
    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl
}