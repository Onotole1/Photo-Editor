package com.example.photoeditor.feature.main.domain.usecase.transform

import com.example.photoeditor.feature.main.domain.entity.BitmapWithId

interface TransformRepository {
    fun saveBitmapToFile(params: BitmapWithId)
}