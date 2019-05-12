package com.spitchenko.photoeditor.feature.main.data.repository.transform.receiver.datasource

import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId

interface TransformSaver {
    fun saveBitmap(params: BitmapWithId, exif: Map<String, String>)
}