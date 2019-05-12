package com.spitchenko.photoeditor.utils

import android.graphics.*
import java.io.File
import java.io.InputStream


fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {

        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}

fun Bitmap.saveToFile(file: File) {
    file.outputStream().use {
        compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
}

fun decodeSampledBitmapFromFile(
    file: File,
    reqWidth: Int,
    reqHeight: Int
): Bitmap {
    return decodeSampledBitmapFromStream(
        {
            file.inputStream()
        },
        reqWidth,
        reqHeight
    )
}

fun decodeSampledBitmapFromStream(
    stream: () -> InputStream?,
    reqWidth: Int,
    reqHeight: Int
): Bitmap {
    // First decode with inJustDecodeBounds=true to check dimensions
    return BitmapFactory.Options().run {
        inJustDecodeBounds = true

        stream().use {
            BitmapFactory.decodeStream(it, null, this)
        }

        // Calculate inSampleSize
        inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        inJustDecodeBounds = false

        stream().use {
            BitmapFactory.decodeStream(it, null, this)
        } ?: throw NullPointerException("Bitmap read error")
    }
}

fun Bitmap.rotate(degree: Float): Bitmap = Matrix().apply {
    postRotate(degree)
}.let {
    Bitmap.createBitmap(this, 0, 0, width, height, it, true)
}

fun Bitmap.toGrayScale(): Bitmap {

    val colorMatrix = ColorMatrix().apply {
        setSaturation(0f)
    }

    val paint = Paint().apply {
        colorFilter = ColorMatrixColorFilter(colorMatrix)
    }

    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also {
        Canvas(it).drawBitmap(this, 0F, 0F, paint)
    }
}

enum class FlipDirection {
    HORIZONTAL,
    VERTICAL
}

fun Bitmap.flip(direction: FlipDirection): Bitmap = Matrix().apply {
    when (direction) {
        FlipDirection.HORIZONTAL -> preScale(-1.0f, 1.0f)
        FlipDirection.VERTICAL -> preScale(1.0f, -1.0f)
    }
}.let {
    Bitmap.createBitmap(this, 0, 0, width, height, it, true)
}