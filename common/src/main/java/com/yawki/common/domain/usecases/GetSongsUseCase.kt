package com.yawki.common.domain.usecases

import com.yawki.common.domain.SafeResult
import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.models.song.Mp3
import com.yawki.common.domain.repositories.SongRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSongsUseCase @Inject constructor(private val songRepository: SongRepository) {

    operator fun invoke(monk: Monk): Flow<SafeResult<List<Mp3>>> {
        return songRepository.getSongList(
            monk = monk,
            forceRefresh = true
        )
    }
}