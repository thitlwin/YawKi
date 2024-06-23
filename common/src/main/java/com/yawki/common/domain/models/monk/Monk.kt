package com.yawki.common.domain.models.monk

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

//@Parcelize
data class Monk(
    val id: Int,
    val name: String,
    val imageUrl: String
) : Serializable