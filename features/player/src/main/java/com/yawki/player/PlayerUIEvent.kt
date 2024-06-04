package com.yawki.player

import com.yawki.common.presentation.PlayerUIState

sealed class PlayerUIEvent {
    data class PlaySong(val mediaItemIndex: Int) : PlayerUIEvent()
    data object PauseSong : PlayerUIEvent()
    data object ResumeSong : PlayerUIEvent()
    data object SkipToNextSong : PlayerUIEvent()
    data object SkipToPreviousSong : PlayerUIEvent()
    data object OnBackPress: PlayerUIEvent()
    data object OnDownload: PlayerUIEvent()

    data class SeekSongToPosition(val position: Long) : PlayerUIEvent()
    data class BindInitialState(val selectedSong: PlayerUIState): PlayerUIEvent()
}