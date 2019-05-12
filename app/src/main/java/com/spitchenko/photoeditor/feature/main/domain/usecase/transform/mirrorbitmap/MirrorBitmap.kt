package com.spitchenko.photoeditor.feature.main.domain.usecase.transform.mirrorbitmap

import android.graphics.Bitmap
import com.spitchenko.photoeditor.feature.main.domain.usecase.getexif.GetExifRepository
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.TransformBitmapDelayed
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.TransformReceiver
import com.spitchenko.domain.usecase.ExecutionThread
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.RandomGenerator
import com.spitchenko.photoeditor.utils.FlipDirection
import com.spitchenko.photoeditor.utils.flip
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named

class MirrorBitmap @Inject constructor(
    @Named("timer_execution_thread")
    timerThreadExecutor: ExecutionThread,
    @Named("worker_execution_thread")
    workerThreadExecutor: ExecutionThread,
    @Named("post_execution_thread")
    postThreadExecutor: ExecutionThread,
    randomGenerator: RandomGenerator<Long>,
    transformRepository: TransformReceiver,
    exifRepository: GetExifRepository
) : TransformBitmapDelayed(
    randomGenerator,
    transformRepository,
    exifRepository,
    timerThreadExecutor,
    workerThreadExecutor,
    postThreadExecutor
) {

    override fun bitmapSource(params: Bitmap) = Observable.fromCallable {
        params.flip(FlipDirection.HORIZONTAL)
    }
}