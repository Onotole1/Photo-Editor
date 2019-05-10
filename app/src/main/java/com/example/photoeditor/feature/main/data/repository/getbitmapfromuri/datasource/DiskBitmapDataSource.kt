package com.example.photoeditor.feature.main.data.repository.getbitmapfromuri.datasource

import android.content.ContentResolver
import android.graphics.Bitmap
import androidx.exifinterface.media.ExifInterface
import com.example.photoeditor.feature.main.data.entity.ReqBitmapSize
import com.example.photoeditor.feature.main.domain.entity.UriWithId
import com.example.photoeditor.shared.domain.model.State
import com.example.photoeditor.utils.copyTo
import com.example.photoeditor.utils.decodeSampledBitmapFromStream
import com.example.photoeditor.utils.exif
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
    override fun getBitmapFromUrl(uriWithId: UriWithId): Observable<State<Bitmap>> {
        return Observable.fromCallable {
            val (reqWidth, reqHeight) = reqBitmapSize

            val stream = {
                contentResolver.openInputStream(uriWithId.uri)
            }

            val bitmap = decodeSampledBitmapFromStream(stream, reqWidth, reqHeight)

            val outputFile = File(imagesPath, uriWithId.itemId.toString())

            bitmap.saveToFile(outputFile)

            stream()?.use {
                ExifInterface(it).copyTo(outputFile.exif())
            }

            State.Data(bitmap)
        }
    }
}