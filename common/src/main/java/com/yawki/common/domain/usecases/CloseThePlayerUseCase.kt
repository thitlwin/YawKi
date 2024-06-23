package com.yawki.common.domain.usecases

import com.yawki.common.domain.service.YawKiPlayerController
import javax.inject.Inject

class CloseThePlayerUseCase @Inject constructor(
    private val yawkiPlayerController: YawKiPlayerController
) {
    operator fun invoke() {
        yawkiPlayerController.stop()
    }
}