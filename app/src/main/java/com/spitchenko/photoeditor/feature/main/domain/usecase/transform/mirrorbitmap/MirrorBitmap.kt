package com.spitchenko.photoeditor.feature.main.domain.usecase.transform.mirrorbitmap

import android.graphics.Bitmap
import com.spitchenko.domain.model.State
import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId
import io.reactivex.Observable

interface MirrorBitmap {

    operator fun invoke(bitmap: BitmapWithId): Observable<State<Bitmap>>
}