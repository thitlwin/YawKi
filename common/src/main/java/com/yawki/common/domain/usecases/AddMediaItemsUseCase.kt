package com.yawki.common.domain.usecases

import com.yawki.common.domain.models.song.Song
import com.yawki.common.domain.service.YawKiPlayerController
import javax.inject.Inject

class AddMediaItemsUseCase @Inject constructor(
    private val yawKiPlayerController: YawKiPlayerController
){

    operator fun invoke(songs: List<Song>) {
        yawKiPlayerController.addMediaItems(songs)
    }
}