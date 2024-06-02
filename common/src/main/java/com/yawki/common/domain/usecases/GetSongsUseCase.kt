package com.yawki.common.domain.usecases

import com.yawki.common.domain.SafeResult
import com.yawki.common.domain.models.song.Song
import com.yawki.common.domain.repositories.SongRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSongsUseCase @Inject constructor(private val songRepository: SongRepository){

    operator fun invoke(monkId: Int): Flow<SafeResult<List<Song>>> {
        return songRepository.getSongList(monkId)
    }
}