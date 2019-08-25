package com.spitchenko.photoeditor.feature.main.domain.usecase.transform.mirrorbitmap

import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.TransformBitmapDelayedDelegate
import com.spitchenko.photoeditor.utils.FlipDirection
import com.spitchenko.photoeditor.utils.flip
import io.reactivex.Observable
import javax.inject.Inject

class MirrorBitmapImpl @Inject constructor(
    private val delegate: TransformBitmapDelayedDelegate
) : MirrorBitmap {

    override fun invoke(bitmap: BitmapWithId) =
        delegate.buildUseCaseObservable(bitmap, Observable.fromCallable {
            bitmap.source.flip(FlipDirection.HORIZONTAL)
        })
}