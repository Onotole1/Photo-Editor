package com.example.photoeditor.feature.main.domain.usecase.invertbitmap

import android.graphics.Bitmap
import com.example.photoeditor.feature.main.domain.usecase.TransformBitmapDelayed
import com.example.photoeditor.shared.domain.usecase.ExecutionThread
import com.example.photoeditor.shared.domain.usecase.RandomGenerator
import com.example.photoeditor.utils.toGrayScale
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named

class InvertBitmap @Inject constructor(
    @Named("worker_execution_thread")
    workerThreadExecutor: ExecutionThread,
    @Named("post_execution_thread")
    postThreadExecutor: ExecutionThread,
    randomGenerator: RandomGenerator<Long>
) : TransformBitmapDelayed(
    randomGenerator,
    workerThreadExecutor,
    postThreadExecutor
) {

    override fun bitmapSource(params: Bitmap) = Observable.fromCallable {
        params.toGrayScale()
    }
}