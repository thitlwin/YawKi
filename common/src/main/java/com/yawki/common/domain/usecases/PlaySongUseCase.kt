package com.yawki.common.domain.usecases

import com.yawki.common.domain.service.YawKiPlayerController
import javax.inject.Inject

class PlaySongUseCase @Inject constructor(
    private val yawKiPlayerController: YawKiPlayerController
) {
    operator fun invoke(mediaItemIndex: Int) = yawKiPlayerController.play(mediaItemIndex)
}