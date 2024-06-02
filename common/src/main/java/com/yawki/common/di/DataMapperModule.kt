package com.yawki.common.di

import com.yawki.common.data.mapper.DomainModelMapper
import com.yawki.common.data.mapper.MonkDomainModelMapper
import com.yawki.common.data.mapper.SongDomainModelMapper
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
}