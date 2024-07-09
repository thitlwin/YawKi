package com.yawki.common.data.models

sealed class Mp3Dto {
    data class SongFolderDto(
        val id: Int = 0,
        val name: String = "",
        val monkId: Int = 0
    ): Mp3Dto()

    data class SongDto(
        val id: Int = 0,
        val name: String = "",
        val serialNo: Int = 0,
        val monkId: Int = 1,
        val monk: String = "",
        val fileUrl: String = "",
        val artworkUri: String = "",
    ): Mp3Dto()
}