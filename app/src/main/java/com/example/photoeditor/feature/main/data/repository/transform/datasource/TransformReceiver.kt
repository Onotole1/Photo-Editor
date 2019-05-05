package com.example.photoeditor.feature.main.data.repository.transform.datasource

import com.example.photoeditor.feature.main.domain.entity.BitmapWithId

interface TransformReceiver {
    fun saveBitmap(transformParams: BitmapWithId)
}