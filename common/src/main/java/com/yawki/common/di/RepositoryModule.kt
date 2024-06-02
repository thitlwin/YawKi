package com.yawki.common.di

import com.google.firebase.firestore.FirebaseFirestore
import com.yawki.common.data.datasource.remote.MonkRemoteDataSource
import com.yawki.common.data.datasource.remote.SongRemoteDataSource
import com.yawki.common.data.mapper.DomainModelMapper
import com.yawki.common.data.models.MonkDto
import com.yawki.common.data.models.SongDto
import com.yawki.common.data.repositories.MonkRepositoryImpl
import com.yawki.common.data.repositories.SongRepositoryImpl
import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.models.song.Song
import com.yawki.common.domain.repositories.MonkRepository
import com.yawki.common.domain.repositories.SongRepository
import com.yawki.common.utils.Constants
import dagger.Binds
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
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()//.collection(Constants.MONK_COLLECTION)

    @Singleton
    @Provides
    fun provideMonkRemoteDataSource(firebaseFirestore: FirebaseFirestore) =
        MonkRemoteDataSource(firebaseFirestore.collection(Constants.MONK_COLLECTION))

    @Provides
    @Singleton
    fun bindMonkRepository(
        monkRemoteDataSource: MonkRemoteDataSource,
        domainMapper: DomainModelMapper<MonkDto, Monk>
    ): MonkRepository = MonkRepositoryImpl(
        monkRemoteDataSource,
        domainMapper
    )

    @Singleton
    @Provides
    fun provideSongRemoteDataSource(firebaseFirestore: FirebaseFirestore) =
        SongRemoteDataSource(firebaseFirestore.collection(Constants.SONG_COLLECTION))


    @Provides
    @Singleton
    fun bindSongRepository(
        songRemoteDataSource: SongRemoteDataSource,
        domainMapper: DomainModelMapper<SongDto, Song>
    ): SongRepository = SongRepositoryImpl(
        songRemoteDataSource,
        domainMapper
    )


//    @Singleton
//    @Provides
//    fun provideMusicRepository(
//        musicRemoteDatabase: MusicRemoteDatabase,
//    ): MusicRepository =
//        MusicRepositoryImpl(musicRemoteDatabase)
}