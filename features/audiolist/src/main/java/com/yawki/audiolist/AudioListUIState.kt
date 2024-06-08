package com.yawki.audiolist

import com.yawki.common.domain.models.monk.Monk
import com.yawki.common.domain.models.song.Song

data class AudioListUIState(
    val loading: Boolean = false,
    val error: Throwable? = null,
    val songs: List<Song>? = emptyList(),
)