package com.yawki.common.domain.usecases

import android.util.Log
import com.yawki.common.domain.service.YawKiPlayerController
import javax.inject.Inject


class PlaySongUseCase @Inject constructor(
    private val yawKiPlayerController: YawKiPlayerController
) {
    private val TAG = "-play song-"
    operator fun invoke(mediaItemIndex: Int){
        Log.d(TAG, "Play song =>$mediaItemIndex")
        yawKiPlayerController.play(mediaItemIndex)
    }
}