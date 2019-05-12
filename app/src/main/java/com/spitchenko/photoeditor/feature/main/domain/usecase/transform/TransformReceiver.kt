package com.spitchenko.photoeditor.feature.main.domain.usecase.transform

import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId

interface TransformReceiver {
    fun saveBitmapToFile(params: BitmapWithId, exif: Map<String, String>)
}