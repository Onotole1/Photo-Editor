package com.example.photoeditor.feature.main.data.repository.getresults.datasource

import com.example.photoeditor.feature.main.data.entity.ReqBitmapSize
import com.example.photoeditor.feature.main.domain.entity.BitmapWithId
import com.example.photoeditor.utils.decodeSampledBitmapFromFile
import io.reactivex.Observable
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class DiskResultsDataSource @Inject constructor(
    @Named("images_dir_result")
    private val imagesPath: File,
    @Named("controller_image_dir")
    private val controllerImagePath: File,
    private val reqBitmapSize: ReqBitmapSize
) : ResultsDataSource {
    override fun getResults(): Observable<List<BitmapWithId>> {
        return Observable.fromCallable {
            val (reqWidth, reqHeight) = reqBitmapSize

            val controllerImage = controllerImagePath.listFiles().firstOrNull()?.let {
                BitmapWithId(
                    it.name.toLong(),
                    decodeSampledBitmapFromFile(
                        it, reqWidth, reqHeight
                    )
                )
            }

            val results = imagesPath.listFiles().map {
                BitmapWithId(
                    it.name.toLong(),
                    decodeSampledBitmapFromFile(
                        it, reqWidth, reqHeight
                    )
                )
            }

            controllerImage?.let {
                results.plus(it)
            } ?: results
        }
    }
}