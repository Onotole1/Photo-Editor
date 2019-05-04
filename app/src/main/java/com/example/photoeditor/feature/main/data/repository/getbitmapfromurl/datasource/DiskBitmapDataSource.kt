package com.example.photoeditor.feature.main.data.repository.getbitmapfromurl.datasource

import android.graphics.Bitmap
import com.example.photoeditor.feature.main.data.entity.ReqBitmapSize
import com.example.photoeditor.shared.domain.model.State
import com.example.photoeditor.utils.decodeSampledBitmapFromFile
import io.reactivex.Observable
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.net.URL
import javax.inject.Inject
import javax.inject.Named

class DiskBitmapDataSource @Inject constructor(
    private val reqBitmapSize: ReqBitmapSize,
    @Named("images_path")
    private val imagesPath: String
) : BitmapDataSource {
    override fun getBitmapFromUrl(url: String): Observable<State<Bitmap>> {
        return Observable.create { emitter ->
            try {

                val imagePath = "$imagesPath/${url.substringAfterLast("/")}"

                val (reqWidth, reqHeight) = reqBitmapSize

                val bitmap =
                    decodeSampledBitmapFromFile(imagePath, reqWidth, reqHeight)

                emitter.onNext(State.Data(bitmap))

                emitter.onComplete()

            } catch (e: Throwable) {
                emitter.onError(e)
            }
        }
    }
}