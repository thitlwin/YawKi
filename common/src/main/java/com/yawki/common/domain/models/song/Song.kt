package com.yawki.common.domain.models.song

import androidx.media3.common.MediaItem

data class Song(
    val id: Int,
    val name: String,
    val serialNo: Int,
    val monkName: String,
    val fileUrl: String,
    val isFavorite: Boolean = false,
    val artworkUri: String,
    var isPlaying: Boolean = false
)

fun MediaItem.toSong() =
    Song(
        id = mediaMetadata.trackNumber ?: 0,
        name = mediaMetadata.title.toString(),
        monkName = mediaMetadata.subtitle.toString(),
        serialNo = 0,
        fileUrl = mediaId,
        artworkUri = if(mediaMetadata.artworkUri == null) "" else mediaMetadata.artworkUri.toString(),
    )