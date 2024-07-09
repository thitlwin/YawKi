package com.yawki.common.di

import android.content.Context
import com.yawki.common.data.datasource.local.database.dao.SongDao
import com.yawki.common.data.mapper.SongEntityMapper
import com.yawki.common.data.service.YawKiPlayerControllerImpl
import com.yawki.common.domain.service.YawKiPlayerController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideYawKiPlayerController(
        @ApplicationContext context: Context,
        songDao: SongDao,
        songEntityMapper: SongEntityMapper
    ): YawKiPlayerController =
        YawKiPlayerControllerImpl(context, songDao, songEntityMapper)
}