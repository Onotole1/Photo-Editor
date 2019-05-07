package com.example.photoeditor.feature.main.data.repository.setcontrollerimage.datasource

import com.example.photoeditor.feature.main.domain.entity.SetImageRequest
import io.reactivex.Completable
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class DiskSetControllerImageSource @Inject constructor(
    @Named("controller_image_dir")
    private val imageControllerPath: File,
    @Named("images_dir_result")
    private val imagesDirResult: File
    ): SetControllerImageSource {

    override fun setControllerImage(request: SetImageRequest): Completable {
        return Completable.fromCallable {
            imageControllerPath.listFiles()?.forEach {
                it.delete()
            }

            val destinationFile = File(imageControllerPath, request.destinationImageId.toString())
            val sourceFile = File(imagesDirResult, request.sourceImageId.toString())

            sourceFile.copyTo(destinationFile)
        }
    }
}