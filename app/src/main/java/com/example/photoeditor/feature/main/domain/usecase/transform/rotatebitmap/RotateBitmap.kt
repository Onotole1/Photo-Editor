package com.example.photoeditor.feature.main.domain.usecase.transform.rotatebitmap

import android.graphics.Bitmap
import com.example.photoeditor.feature.main.domain.usecase.getexif.GetExifRepository
import com.example.photoeditor.feature.main.domain.usecase.transform.TransformBitmapDelayed
import com.example.photoeditor.feature.main.domain.usecase.transform.TransformReceiver
import com.spitchenko.domain.usecase.ExecutionThread
import com.example.photoeditor.feature.main.domain.usecase.transform.RandomGenerator
import com.example.photoeditor.utils.rotate
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named

class RotateBitmap @Inject constructor(
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
        params.rotate(DEGREE)
    }

    private companion object {
        const val DEGREE = 90F
    }
}