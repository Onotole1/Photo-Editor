package com.spitchenko.photoeditor.feature.main.domain.usecase.transform.invertbitmap

import android.graphics.Bitmap
import com.spitchenko.domain.model.State
import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId
import io.reactivex.Observable

interface InvertBitmap {

    operator fun invoke(bitmap: BitmapWithId): Observable<State<Bitmap>>
}