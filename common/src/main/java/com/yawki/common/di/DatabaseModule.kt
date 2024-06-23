package com.yawki.common.di

import android.content.Context
import androidx.room.Room
import com.yawki.common.data.datasource.local.database.YawkiDatabase
import com.yawki.common.data.datasource.local.database.dao.MonkDao
import com.yawki.common.data.datasource.local.database.dao.SongDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): YawkiDatabase {
        return Room.databaseBuilder(
            context,
            YawkiDatabase::class.java,
            "yawki_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideSongDao(yawkiDatabase: YawkiDatabase): SongDao = yawkiDatabase.songDao()

    @Provides
    @Singleton
    fun provideMonkDao(yawkiDatabase: YawkiDatabase): MonkDao = yawkiDatabase.monkDao()
}