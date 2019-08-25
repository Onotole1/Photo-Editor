package com.spitchenko.photoeditor.feature.main.domain.usecase.transform.rotatebitmap

import android.graphics.Bitmap
import com.spitchenko.domain.model.State
import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId
import io.reactivex.Observable

interface RotateBitmap {

    operator fun invoke(bitmap: BitmapWithId): Observable<State<Bitmap>>
}