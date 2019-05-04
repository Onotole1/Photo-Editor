package com.example.photoeditor.feature.main.domain.usecase.getbitmapfromuri

import android.graphics.Bitmap
import android.net.Uri

interface BitmapFromUriRepository {
    fun getBitmapFromUri(uri: Uri): Bitmap
}