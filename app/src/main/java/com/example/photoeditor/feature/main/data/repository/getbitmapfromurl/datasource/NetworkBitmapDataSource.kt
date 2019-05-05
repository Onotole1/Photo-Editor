package com.example.photoeditor.feature.main.data.repository.getbitmapfromurl.datasource

import android.graphics.Bitmap
import com.example.photoeditor.feature.main.data.entity.ReqBitmapSize
import com.example.photoeditor.shared.domain.model.State
import com.example.photoeditor.utils.decodeSampledBitmapFromFile
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
    @Named("images_path")
    private val imagesPath: String
) : BitmapDataSource {
    override fun getBitmapFromUrl(url: String): Observable<State<Bitmap>> {
        return Observable.create { emitter ->
            val imagePath = "$imagesPath/${url.substringAfterLast("/")}"

            var connection: HttpURLConnection? = null
            try {
                val wrappedUrl = URL(url)

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
                                File(imagePath).delete()
                                return@create
                            }
                        }

                        output.flush()

                        val (reqWidth, reqHeight) = reqBitmapSize

                        val bitmap =
                            decodeSampledBitmapFromFile(imagePath, reqWidth, reqHeight)

                        emitter.onNext(State.Data(bitmap))

                        emitter.onComplete()
                    }
                }
            } catch (e: Throwable) {
                if (e is NullPointerException) {
                    File(imagePath).delete()
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