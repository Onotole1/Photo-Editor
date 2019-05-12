package com.spitchenko.photoeditor.feature.main.data.repository.getbitmapfromuri.datasource

import android.graphics.Bitmap
import com.spitchenko.photoeditor.feature.main.domain.entity.UriWithId
import com.spitchenko.domain.model.State
import io.reactivex.Observable

interface BitmapDataSource {
    fun getBitmapFromUrl(uriWithId: UriWithId): Observable<State<Bitmap>>
}