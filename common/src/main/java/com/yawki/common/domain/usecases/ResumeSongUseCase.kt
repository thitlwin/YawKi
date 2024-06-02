package com.yawki.common.domain.usecases

import com.yawki.common.domain.service.YawKiPlayerController
import javax.inject.Inject

class ResumeSongUseCase @Inject constructor(
    private val yawKiPlayerController: YawKiPlayerController
) {
    operator fun invoke() = yawKiPlayerController.resume()
}