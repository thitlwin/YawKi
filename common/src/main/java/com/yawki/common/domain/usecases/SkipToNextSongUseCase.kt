package com.yawki.common.domain.usecases

import com.yawki.common.domain.models.song.Song
import com.yawki.common.domain.service.YawKiPlayerController
import javax.inject.Inject

class SkipToNextSongUseCase @Inject constructor(
    private val yawKiPlayerController: YawKiPlayerController
) {
    operator fun invoke(updateUI: (Song?) -> Unit) {
        yawKiPlayerController.skipToNextSong()
        updateUI(yawKiPlayerController.getCurrentSong())
    }
}