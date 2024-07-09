package com.yawki.common.domain.repositories

import com.yawki.common.domain.SafeResult
import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.models.song.Mp3
import com.yawki.common.domain.models.song.Mp3.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    fun getSongList(
        monk: Monk,
        forceRefresh: Boolean
    ): Flow<SafeResult<List<Mp3>>>

    suspend fun updateSong(song: Song, monk: Monk): Long
    suspend fun updateCurrentPosition(songID: Int, currentPosition: Long)
}