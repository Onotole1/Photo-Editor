package com.example.photoeditor.feature.main.domain.usecase.transform.invertbitmap

import android.graphics.Bitmap
import com.example.photoeditor.feature.main.domain.usecase.getexif.GetExifRepository
import com.example.photoeditor.feature.main.domain.usecase.transform.TransformBitmapDelayed
import com.example.photoeditor.feature.main.domain.usecase.transform.TransformReceiver
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
    randomGenerator: RandomGenerator<Long>,
    transformReceiver: TransformReceiver,
    exifRepository: GetExifRepository
) : TransformBitmapDelayed(
    randomGenerator,
    transformReceiver,
    exifRepository,
    workerThreadExecutor,
    postThreadExecutor
) {

    override fun bitmapSource(params: Bitmap) = Observable.fromCallable {
        params.toGrayScale()
    }
}