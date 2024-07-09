package com.yawki.audiolist

import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.models.song.Mp3.Song

sealed class AudioListUIEvent {
    data class FetchSong(val monk: Monk) : AudioListUIEvent()
    data class OnSongClick(val selectedSong: Song) : AudioListUIEvent()
    data class OnFavoriteIconClick(val selectedSong: Song) : AudioListUIEvent()
    data object OnBackPress: AudioListUIEvent()
}