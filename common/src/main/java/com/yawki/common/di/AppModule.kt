package com.yawki.common.di

import android.content.Context
import com.yawki.common.data.service.YawKiPlayerControllerImpl
import com.yawki.common.domain.service.YawKiPlayerController
import com.yawki.common.presentation.EventBus
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
    fun provideYawKiPlayerController(@ApplicationContext context: Context): YawKiPlayerController =
        YawKiPlayerControllerImpl(context)

    @Singleton
    @Provides
    fun provideEventBus(): EventBus = EventBus()
}