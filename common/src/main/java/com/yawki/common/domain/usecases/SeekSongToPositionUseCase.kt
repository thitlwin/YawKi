package com.yawki.common.domain.usecases

import com.yawki.common.domain.service.YawKiPlayerController
import javax.inject.Inject

class SeekSongToPositionUseCase @Inject constructor(
    private val yawKiPlayerController: YawKiPlayerController
) {
    operator fun invoke(position: Long) {
        yawKiPlayerController.seekTo(position)
    }
}