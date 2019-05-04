package com.example.photoeditor.feature.main.data.repository.getbitmapfromuri

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.example.photoeditor.feature.main.data.entity.ReqBitmapSize
import com.example.photoeditor.feature.main.domain.usecase.getbitmapfromuri.BitmapFromUriRepository
import com.example.photoeditor.utils.decodeSampledBitmapFromUri

class BitmapFromUriRepositoryImpl(
    private val contentResolver: ContentResolver,
    private val reqBitmapSize: ReqBitmapSize
) : BitmapFromUriRepository {

    override fun getBitmapFromUri(uri: Uri): Bitmap {
        val (reqWidth, reqHeight) = reqBitmapSize

        return decodeSampledBitmapFromUri(contentResolver, uri, reqWidth, reqHeight)
    }
}