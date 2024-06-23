package com.yawki.common.di

import com.yawki.common.data.datasource.local.database.models.MonkEntity
import com.yawki.common.data.datasource.local.database.models.SongEntity
import com.yawki.common.data.mapper.DomainModelMapper
import com.yawki.common.data.mapper.EntityMapper
import com.yawki.common.data.mapper.MonkDomainModelMapper
import com.yawki.common.data.mapper.MonkEntityMapper
import com.yawki.common.data.mapper.SongDomainModelMapper
import com.yawki.common.data.mapper.SongEntityMapper
import com.yawki.common.data.models.MonkDto
import com.yawki.common.data.models.SongDto
import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.models.song.Song
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataMapperModule {
    @Binds
    @Singleton
    abstract fun bindMonkDomainMapper(monkDomainModelMapper: MonkDomainModelMapper): DomainModelMapper<MonkDto, Monk>

    @Binds
    @Singleton
    abstract fun bindSongDomainMapper(songDomainModelMapper: SongDomainModelMapper): DomainModelMapper<SongDto, Song>

    @Binds
    @Singleton
    abstract fun bindSongEntityMapper(songEntityMapper: SongEntityMapper): EntityMapper<Song, SongEntity>

    @Binds
    @Singleton
    abstract fun bindMonkEntityMapper(monkEntityMapper: MonkEntityMapper): EntityMapper<Monk, MonkEntity>
}