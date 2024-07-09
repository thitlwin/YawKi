package com.yawki.common.domain.models.song

import androidx.media3.common.MediaItem

sealed class Mp3 {
    data class Song(
        val id: Int,
        val name: String,
        val serialNo: Int,
        val monkName: String,
        val fileUrl: String,
        val isFavorite: Boolean = false,
        val artworkUri: String,
        val isPlaying: Boolean = false,
        val currentPosition: Long = 0L,
        val itemType: ItemType = ItemType.SONG,
        val folderId: Int = 0
    ): Mp3()

    data class SongFolder(
        val id: Int,
        val name: String,
        val monkId: Int
    ): Mp3()

}


enum class ItemType(val value: String = "S") {
    SONG,
    FOLDER;

    companion object {
        fun fromChar(value: String): ItemType {
            return when (value) {
                "S" -> SONG
                "F" -> FOLDER
                else -> throw IllegalArgumentException("'$value' is not a valid ItemType")
            }
        }
    }
}

fun MediaItem.toSong() =
    Mp3.Song(
        id = mediaMetadata.trackNumber ?: 0,
        name = mediaMetadata.title.toString(),
        monkName = mediaMetadata.subtitle.toString(),
        serialNo = 0,
        fileUrl = mediaId,
        artworkUri = if (mediaMetadata.artworkUri == null) "" else mediaMetadata.artworkUri.toString(),
    )