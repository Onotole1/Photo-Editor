package com.example.photoeditor.feature.main.domain.usecase.transform

import android.graphics.Bitmap
import androidx.exifinterface.media.ExifInterface
import com.example.photoeditor.BuildConfig
import com.example.photoeditor.feature.main.domain.entity.BitmapWithId
import com.example.photoeditor.feature.main.domain.usecase.getexif.GetExifRepository
import com.spitchenko.domain.model.State
import com.spitchenko.domain.usecase.ExecutionThread
import com.spitchenko.domain.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit

abstract class TransformBitmapDelayed(
    private val randomGenerator: RandomGenerator<Long>,
    private val receiver: TransformReceiver,
    private val repository: GetExifRepository,
    workerThreadExecutor: ExecutionThread,
    postThreadExecutor: ExecutionThread
) : UseCase<State<Bitmap>, BitmapWithId>(workerThreadExecutor, postThreadExecutor) {

    abstract fun bitmapSource(params: Bitmap): Observable<Bitmap>

    override fun buildUseCaseObservable(params: BitmapWithId): Observable<State<Bitmap>> {
        val random = randomGenerator.generate()
        val max = random.dec()

        val interval = Observable.interval(1, TimeUnit.SECONDS).take(random)

        return Observable.combineLatest(
            interval,
            bitmapSource(params.source).flatMap { bitmap ->
                repository.getExif().map { exif ->
                    receiver.saveBitmapToFile(params.copy(source = bitmap), exif.toMutableMap().apply {
                        set(ExifInterface.TAG_IMAGE_LENGTH, bitmap.height.toString())
                        set(ExifInterface.TAG_IMAGE_WIDTH, bitmap.width.toString())
                        set(ExifInterface.TAG_MODEL, BuildConfig.APPLICATION_ID)
                    })
                    bitmap
                }.toObservable()
            },
            BiFunction<Long, Bitmap, State<Bitmap>> { seconds: Long, bitmap: Bitmap ->
                if (max == seconds) {
                    State.Data(bitmap)
                } else {
                    val progress = seconds / random.toFloat() * 100F

                    State.Progress(progress.toInt())
                }
            })
    }
}