package com.example.photoeditor.feature.main.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.example.photoeditor.feature.main.domain.usecase.BitmapFromUriRepository
import com.example.photoeditor.utils.decodeSampledBitmapFromUri

class BitmapFromUriRepositoryImpl(private val context: Context): BitmapFromUriRepository {

    override fun getBitmapFromUri(uri: Uri, reqWidth: Int, reqHeight: Int): Bitmap {
        return decodeSampledBitmapFromUri(context.contentResolver, uri, reqWidth, reqHeight)
    }
}