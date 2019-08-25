package com.spitchenko.photoeditor.feature.main.domain.usecase.transform.rotatebitmap

import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.TransformBitmapDelayedDelegate
import com.spitchenko.photoeditor.utils.rotate
import io.reactivex.Observable
import javax.inject.Inject

class RotateBitmapImpl @Inject constructor(
    private val delegate: TransformBitmapDelayedDelegate
) : RotateBitmap {

    override fun invoke(bitmap: BitmapWithId) =
        delegate.buildUseCaseObservable(bitmap, Observable.fromCallable {
            bitmap.source.rotate(DEGREE)
        })

    private companion object {
        const val DEGREE = 90F
    }
}