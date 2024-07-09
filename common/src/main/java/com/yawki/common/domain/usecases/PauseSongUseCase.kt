package com.yawki.common.domain.usecases

import com.yawki.common.data.datasource.local.database.dao.SongDao
import com.yawki.common.domain.service.YawKiPlayerController
import javax.inject.Inject

class PauseSongUseCase @Inject constructor(
    private val yawKiPlayerController: YawKiPlayerController,
    private val songDao: SongDao,
    private val persistCurrentSongPositionUseCase:PersistCurrentSongPositionUseCase
){
    suspend operator fun invoke(){
        songDao.resetAllSongsAsNotPlayed()
        yawKiPlayerController.pause()
        persistCurrentSongPositionUseCase.invoke()
    }
}