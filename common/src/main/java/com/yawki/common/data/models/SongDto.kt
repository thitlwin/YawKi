package com.yawki.common.data.models

data class SongDto(
    val id: Int = 0,
    val name: String = "",
    val serialNo: Int = 0,
    val monkId: Int = 1,
    val monk: String = "",
    val fileUrl: String = "",
    val artworkUri: String = ""
)