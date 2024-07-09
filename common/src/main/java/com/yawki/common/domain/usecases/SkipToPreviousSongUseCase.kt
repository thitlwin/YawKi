package com.yawki.common.domain.usecases

import com.yawki.common.domain.models.song.Mp3.Song
import com.yawki.common.domain.service.YawKiPlayerController
import javax.inject.Inject

class SkipToPreviousSongUseCase @Inject constructor(
    private val yawKiPlayerController: YawKiPlayerController
) {
    operator fun invoke() {
        yawKiPlayerController.skipToPreviousSong()
//        updateUI(yawKiPlayerController.getCurrentSong())
    }
}