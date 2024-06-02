package com.yawki.common.domain.models.song

import androidx.media3.common.MediaItem


data class Song(
    val id: String,
    val name: String,
    val serialNo: Int,
    val monk: String,
    val fileUrl: String,
    val isFavorite: Boolean = false
)


fun MediaItem.toSong() =
    Song(
        id = mediaId,
        name = mediaMetadata.title.toString(),
        monk = "",
        serialNo = 0,
        fileUrl = mediaId,
    )

//fun MediaItem.toSong() =
//    Song(
//        mediaId = mediaId,
//        title = mediaMetadata.title.toString(),
//        subtitle = mediaMetadata.subtitle.toString(),
//        songUrl = mediaId,
//        imageUrl = mediaMetadata.artworkUri.toString()
//    )