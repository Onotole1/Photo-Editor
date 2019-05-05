package com.example.photoeditor.feature.main.data.repository.transform

import android.graphics.Bitmap
import com.example.photoeditor.feature.main.domain.entity.BitmapWithId
import com.example.photoeditor.feature.main.domain.usecase.transform.TransformRepository
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Named

class TransformRepositoryImpl @Inject constructor(
    @Named("images_dir_result")
    private val imagesPath: File
) : TransformRepository {
    override fun saveBitmapToFile(params: BitmapWithId) {
        val outputFile = File(imagesPath, params.imageId.toString())

        FileOutputStream(outputFile).use {
            params.source.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }
}