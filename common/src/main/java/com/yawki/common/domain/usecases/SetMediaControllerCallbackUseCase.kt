package com.yawki.common.domain.usecases

import android.util.Log
import com.yawki.common.domain.models.PlayerState
import com.yawki.common.domain.models.song.Song
import com.yawki.common.domain.service.YawKiPlayerController
import javax.inject.Inject

class SetMediaControllerCallbackUseCase @Inject constructor(
    private val yawKiPlayerController: YawKiPlayerController
){
    operator fun invoke(
        callback: (
            playerState: PlayerState,
            currentSong: Song?,
            currentPosition: Long,
            totalDuration: Long,
            isShuffleEnabled: Boolean,
            isRepeatOneEnabled: Boolean
                ) -> Unit
    ) {
//        Log.d("callback", "SetMediaControllerCallbackUseCase => usecase.invoke()")
        yawKiPlayerController.mediaControllerCallback = callback
    }
}