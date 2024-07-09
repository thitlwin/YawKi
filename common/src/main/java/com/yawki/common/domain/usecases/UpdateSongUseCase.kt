package com.yawki.common.domain.usecases

import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.models.song.Mp3.Song
import com.yawki.common.domain.repositories.SongRepository
import javax.inject.Inject

class UpdateSongUseCase @Inject constructor(private val songRepository: SongRepository) {

    suspend operator fun invoke(song: Song, monk: Monk): Long {
        return songRepository.updateSong(song, monk)
    }
}