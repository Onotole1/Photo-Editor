package com.example.photoeditor.feature.main.data.repository.transform.datasource

import com.example.photoeditor.feature.main.domain.entity.BitmapWithId
import java.io.FileOutputStream
import javax.inject.Named

class DiskBitmapReceiver(
    @Named("images_path_result")
    private val imagesPath: String
): TransformReceiver {
    override fun saveBitmap(transformParams: BitmapWithId) {
        FileOutputStream("$imagesPath/${transformParams.imageId}")
    }
}