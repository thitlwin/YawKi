package com.yawki.audiolist

import com.yawki.common.domain.models.song.Mp3

data class AudioListUIState(
    val loading: Boolean = false,
    val error: Throwable? = null,
    val songs: List<Mp3>? = emptyList(),
)