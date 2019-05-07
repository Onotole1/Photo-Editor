package com.example.photoeditor.feature.main.domain.usecase.getbitmapfromuri

import android.graphics.Bitmap
import com.example.photoeditor.feature.main.domain.entity.UriWithId
import com.example.photoeditor.shared.domain.model.State
import io.reactivex.Observable

interface BitmapFromUriRepository {
    fun getBitmapFromUri(uri: UriWithId): Observable<State<Bitmap>>
}