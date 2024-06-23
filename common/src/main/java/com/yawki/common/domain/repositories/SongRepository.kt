package com.yawki.common.domain.repositories

import com.yawki.common.domain.SafeResult
import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.models.song.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    fun getSongList(
        monk: Monk,
        forceRefresh: Boolean
    ): Flow<SafeResult<List<Song>>>

    suspend fun updateSong(song: Song, monk: Monk): Long
}