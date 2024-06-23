package com.yawki.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.yawki.common.R
import java.io.ByteArrayOutputStream

fun getDefaultArtWork(context: Context): ByteArray {
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.little_monk)
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
    return outputStream.toByteArray()
}