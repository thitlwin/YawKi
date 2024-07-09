package com.yawki.common.domain.usecases

import com.yawki.common.data.datasource.local.database.dao.SongDao
import com.yawki.common.domain.service.YawKiPlayerController
import javax.inject.Inject


class PlaySongUseCase @Inject constructor(
    private val yawKiPlayerController: YawKiPlayerController,
    private val songDao: SongDao
) {
    suspend operator fun invoke(mediaItemIndex: Int) {
        songDao.resetAllSongsAsNotPlayed()
        yawKiPlayerController.play(mediaItemIndex)
        val currentSong = yawKiPlayerController.getCurrentSong()
        currentSong?.let {
            yawKiPlayerController.seekTo(position = it.currentPosition)
            songDao.setCurrentSongAsPlayed(it.id)
        }
    }
}