package com.example.photoeditor.feature.main.data.repository.transform

import com.example.photoeditor.feature.main.domain.entity.BitmapWithId
import com.example.photoeditor.feature.main.domain.usecase.transform.TransformRepository
import com.example.photoeditor.utils.saveToFile
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class TransformRepositoryImpl @Inject constructor(
    @Named("images_dir_result")
    private val imagesPath: File
) : TransformRepository {
    override fun saveBitmapToFile(params: BitmapWithId) {
        val outputFile = File(imagesPath, params.imageId.toString())

        params.source.saveToFile(outputFile)
    }
}