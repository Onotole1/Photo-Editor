package com.spitchenko.photoeditor.feature.main.domain.usecase.getbitmapfromuri

import android.graphics.Bitmap
import com.spitchenko.photoeditor.feature.main.domain.entity.UriWithId
import com.spitchenko.domain.model.State
import io.reactivex.Observable

interface BitmapFromUriRepository {
    fun getBitmapFromUri(uri: UriWithId): Observable<State<Bitmap>>
}