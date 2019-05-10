package com.example.photoeditor.feature.main.domain.usecase.transform

import com.example.photoeditor.feature.main.domain.entity.BitmapWithId

interface TransformReceiver {
    fun saveBitmapToFile(params: BitmapWithId, exif: Map<String, String>)
}