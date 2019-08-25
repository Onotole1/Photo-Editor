package com.spitchenko.photoeditor.feature.main.domain.usecase.getbitmapfromuri

import android.graphics.Bitmap
import com.spitchenko.domain.model.State
import com.spitchenko.photoeditor.feature.main.domain.entity.UriWithId
import io.reactivex.Observable

interface GetBitmapFromUri {

    operator fun invoke(params: UriWithId): Observable<State<Bitmap>>
}