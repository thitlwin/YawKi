package com.yawki.common.presentation

import com.yawki.common.domain.models.PlayerState
import com.yawki.common.domain.models.song.Song

data class PlayerUIState(
    val songs: List<Song>? = emptyList(),
    val playerState: PlayerState? = null,
    val currentSong: Song? = null,
    val currentPosition: Long = 0L,
    val totalDuration: Long = 0L,
    val isShuffleEnabled: Boolean = false,
    val isRepeatOneEnabled: Boolean = false
)