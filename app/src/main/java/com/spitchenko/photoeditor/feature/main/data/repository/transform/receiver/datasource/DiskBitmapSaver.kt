package com.spitchenko.photoeditor.feature.main.data.repository.transform.receiver.datasource

import androidx.exifinterface.media.ExifInterface
import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId
import com.spitchenko.photoeditor.utils.saveToFile
import com.spitchenko.photoeditor.utils.setAttributes
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class DiskBitmapSaver @Inject constructor(
    @Named("images_dir_result")
    private val imagesPath: File
): TransformSaver {

    override fun saveBitmap(params: BitmapWithId, exif: Map<String, String>) {
        val outputFile = File(imagesPath, params.imageId.toString())

        params.source.saveToFile(outputFile)

        ExifInterface(outputFile.absolutePath).setAttributes(exif)
    }
}