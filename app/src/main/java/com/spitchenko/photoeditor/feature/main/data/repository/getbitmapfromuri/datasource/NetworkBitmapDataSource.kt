package com.spitchenko.photoeditor.feature.main.data.repository.getbitmapfromuri.datasource

import android.graphics.Bitmap
import androidx.exifinterface.media.ExifInterface
import com.spitchenko.photoeditor.feature.main.data.entity.ReqBitmapSize
import com.spitchenko.photoeditor.feature.main.domain.entity.UriWithId
import com.spitchenko.domain.model.State
import com.spitchenko.photoeditor.utils.copyTo
import com.spitchenko.photoeditor.utils.decodeSampledBitmapFromFile
import io.reactivex.Observable
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Named

class NetworkBitmapDataSource @Inject constructor(
    private val reqBitmapSize: ReqBitmapSize,
    @Named("controller_image_dir")
    private val imagesPath: File
) : BitmapDataSource {
    override fun getBitmapFromUrl(uriWithId: UriWithId): Observable<State<Bitmap>> {
        return Observable.create { emitter ->

            val imagePath = File(imagesPath, uriWithId.itemId.toString())

            var connection: HttpURLConnection? = null
            try {
                val wrappedUrl = URL(uriWithId.uri.toString())

                connection = (wrappedUrl.openConnection() as HttpURLConnection).apply {
                    connectTimeout = CONNECT_TIMEOUT_MILLIS
                    readTimeout = READ_TIMEOUT_MILLIS
                    connect()
                }

                val lengthOfFile = connection.contentLength

                BufferedInputStream(wrappedUrl.openStream(), BUFFER_SIZE_BYTES).use { input ->

                    FileOutputStream(imagePath).use { output ->

                        val data = ByteArray(OUTPUT_ARRAY_SIZE_BYTES)

                        var total: Long = 0

                        var count = 0
                        var progress = 0
                        while (count >= 0) {
                            total += count

                            val currentProgress = progress
                            progress = (total * 100 / lengthOfFile).toInt()
                            if (progress > currentProgress) {
                                emitter.onNext(State.Progress(progress))
                            }

                            output.write(data, 0, count)

                            count = input.read(data)

                            if (emitter.isDisposed) {
                                imagePath.delete()
                                return@create
                            }
                        }

                        output.flush()

                        val (reqWidth, reqHeight) = reqBitmapSize

                        val bitmap =
                            decodeSampledBitmapFromFile(imagePath, reqWidth, reqHeight)

                        ExifInterface(input).copyTo(imagePath.absolutePath)

                        emitter.onNext(State.Data(bitmap))

                        emitter.onComplete()
                    }
                }
            } catch (e: Throwable) {
                if (e is NullPointerException) {
                    imagePath.delete()
                }

                emitter.onError(e)
            } finally {
                connection?.disconnect()
            }
        }
    }

    private companion object {
        const val BUFFER_SIZE_BYTES = 8192
        const val OUTPUT_ARRAY_SIZE_BYTES = 1024
        const val CONNECT_TIMEOUT_MILLIS = 15000
        const val READ_TIMEOUT_MILLIS = 30000
    }
}