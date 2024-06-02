package com.yawki.common.domain.repositories

import com.yawki.common.domain.SafeResult
import com.yawki.common.domain.models.song.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    fun getSongList(monkId: Int): Flow<SafeResult<List<Song>>>
}