package com.example.photoeditor.feature.main.domain.usecase.getbitmap

import android.graphics.Bitmap
import android.net.Uri

interface BitmapFromUriRepository {
    fun getBitmapFromUri(uri: Uri, reqWidth: Int, reqHeight: Int): Bitmap
}