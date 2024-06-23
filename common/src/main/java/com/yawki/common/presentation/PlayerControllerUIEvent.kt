package com.yawki.common.presentation

sealed class PlayerControllerUIEvent {
    data class PlaySong(val mediaItemIndex: Int) : PlayerControllerUIEvent()
    data object PauseSong : PlayerControllerUIEvent()
    data object ResumeSong : PlayerControllerUIEvent()
    data object SkipToNextSong : PlayerControllerUIEvent()
    data object SkipToPreviousSong : PlayerControllerUIEvent()
    data class SeekSongToPosition(val position: Long) : PlayerControllerUIEvent()
    data object OpenFullScreenPlayer: PlayerControllerUIEvent()

    data object CloseThePlayer: PlayerControllerUIEvent()
}