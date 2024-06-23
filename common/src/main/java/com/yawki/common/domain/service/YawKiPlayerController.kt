package com.yawki.common.domain.service

import com.yawki.common.domain.models.PlayerState
import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.models.song.Song

interface YawKiPlayerController {

    var mediaControllerCallback: (
        (
        playerState: PlayerState,
        currentMusic: Song?,
        currentPosition: Long,
        totalDuration: Long,
        isShuffleEnabled: Boolean,
        isRepeatOneEnabled: Boolean
    ) -> Unit
    )?

    fun addMediaItems(songs: List<Song>, monk: Monk)

    fun play(mediaItemIndex: Int)

    fun resume()

    fun pause()

    fun getCurrentPosition(): Long

    fun destroy()

    fun skipToNextSong()

    fun skipToPreviousSong()

    fun getCurrentSong(): Song?

    fun seekTo(position: Long)

    fun stop()
}