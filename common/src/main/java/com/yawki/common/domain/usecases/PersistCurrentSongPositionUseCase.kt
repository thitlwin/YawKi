package com.yawki.common.domain.usecases

import com.yawki.common.domain.repositories.SongRepository
import com.yawki.common.domain.service.YawKiPlayerController
import javax.inject.Inject

class PersistCurrentSongPositionUseCase @Inject constructor(
    private val yawKiPlayerController: YawKiPlayerController,
    private val songRepository: SongRepository
) {
    suspend operator fun invoke() {
        val currentPosition = yawKiPlayerController.getCurrentPosition()
        val currentSong = yawKiPlayerController.getCurrentSong()
        currentSong?.let { song ->
            songRepository.updateCurrentPosition(song.id, currentPosition)
        }
    }
}