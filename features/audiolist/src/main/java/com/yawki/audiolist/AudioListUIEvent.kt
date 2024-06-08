package com.yawki.audiolist

import com.yawki.common.domain.models.song.Song

sealed class AudioListUIEvent {
    data class FetchSong(val monkId: Int) : AudioListUIEvent()
    data class OnSongClick(val selectedSong: Song) : AudioListUIEvent()
    data class OnFavoriteIconClick(val selectedSong: Song) : AudioListUIEvent()
    data object OnBackPress: AudioListUIEvent()
}