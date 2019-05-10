package com.example.photoeditor.feature.main.data.repository.transform.receiver

import com.example.photoeditor.feature.main.data.repository.transform.receiver.datasource.TransformSaver
import com.example.photoeditor.feature.main.domain.entity.BitmapWithId
import com.example.photoeditor.feature.main.domain.usecase.transform.TransformReceiver
import javax.inject.Inject

class TransformReceiverImpl @Inject constructor(
    private val diskBitmapReceiver: TransformSaver
) : TransformReceiver {
    override fun saveBitmapToFile(params: BitmapWithId, exif: Map<String, String>) {
        diskBitmapReceiver.saveBitmap(params, exif)
    }

}