package com.yawki.audiolist

import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.models.song.Song

data class AudioListUIState(
    val loading: Boolean? = false,
    val songs: List<Song>? = emptyList(),
//    val selectedMonk: Monk? = null,
//    val selectedSong: Song? = null,
    val errorMessage: String? = null
)