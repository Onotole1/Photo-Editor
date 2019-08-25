package com.spitchenko.photoeditor.feature.main.domain.usecase.transform

import android.graphics.Bitmap
import androidx.exifinterface.media.ExifInterface
import com.spitchenko.domain.model.State
import com.spitchenko.photoeditor.BuildConfig
import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId
import com.spitchenko.photoeditor.feature.main.domain.usecase.getexif.GetExifRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class TransformBitmapDelayedDelegate @Inject constructor(
    private val randomGenerator: RandomGenerator<Long>,
    private val randomSource: RandomSource,
    private val receiver: TransformReceiver,
    private val repository: GetExifRepository
) {

    fun buildUseCaseObservable(
        params: BitmapWithId,
        bitmapSource: Observable<Bitmap>
    ): Observable<State<Bitmap>> {
        val random = randomGenerator.generate()
        val max = random.dec()

        val interval = randomSource(random)

        return Observable.combineLatest(
            interval,
            bitmapSource.flatMap { bitmap ->
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