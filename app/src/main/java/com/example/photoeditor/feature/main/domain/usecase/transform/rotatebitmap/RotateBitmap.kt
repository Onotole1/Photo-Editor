package com.example.photoeditor.feature.main.domain.usecase.transform.rotatebitmap

import android.graphics.Bitmap
import com.example.photoeditor.feature.main.domain.usecase.transform.TransformBitmapDelayed
import com.example.photoeditor.feature.main.domain.usecase.transform.TransformRepository
import com.example.photoeditor.shared.domain.usecase.ExecutionThread
import com.example.photoeditor.shared.domain.usecase.RandomGenerator
import com.example.photoeditor.utils.rotate
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named

class RotateBitmap @Inject constructor(
    @Named("worker_execution_thread")
    workerThreadExecutor: ExecutionThread,
    @Named("post_execution_thread")
    postThreadExecutor: ExecutionThread,
    randomGenerator: RandomGenerator<Long>,
    transformRepository: TransformRepository
) : TransformBitmapDelayed(
    randomGenerator,
    transformRepository,
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