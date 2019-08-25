package com.spitchenko.photoeditor.feature.main.domain.usecase.transform.invertbitmap

import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.TransformBitmapDelayedDelegate
import com.spitchenko.photoeditor.utils.toGrayScale
import io.reactivex.Observable
import javax.inject.Inject

class InvertBitmapImpl @Inject constructor(
    private val delegate: TransformBitmapDelayedDelegate
) : InvertBitmap {

    override fun invoke(bitmap: BitmapWithId) = delegate.buildUseCaseObservable(
        bitmap,
        Observable.fromCallable {
            bitmap.source.toGrayScale()
        }
    )
}