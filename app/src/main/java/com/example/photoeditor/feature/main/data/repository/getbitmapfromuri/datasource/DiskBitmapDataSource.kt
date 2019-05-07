package com.example.photoeditor.feature.main.data.repository.getbitmapfromuri.datasource

import android.content.ContentResolver
import android.graphics.Bitmap
import com.example.photoeditor.feature.main.data.entity.ReqBitmapSize
import com.example.photoeditor.feature.main.domain.entity.UriWithId
import com.example.photoeditor.shared.domain.model.State
import com.example.photoeditor.utils.decodeSampledBitmapFromUri
import com.example.photoeditor.utils.saveToFile
import io.reactivex.Observable
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class DiskBitmapDataSource @Inject constructor(
    private val reqBitmapSize: ReqBitmapSize,
    @Named("controller_image_dir")
    private val imagesPath: File,
    private val contentResolver: ContentResolver
) : BitmapDataSource {
    override fun getBitmapFromUrl(uri: UriWithId): Observable<State<Bitmap>> {
        return Observable.fromCallable {
            val (reqWidth, reqHeight) = reqBitmapSize

            val bitmap = decodeSampledBitmapFromUri(contentResolver, uri.uri, reqWidth, reqHeight)

            val outputFile = File(imagesPath, uri.itemId.toString())

            bitmap.saveToFile(outputFile)

            State.Data(bitmap)
        }
    }
}