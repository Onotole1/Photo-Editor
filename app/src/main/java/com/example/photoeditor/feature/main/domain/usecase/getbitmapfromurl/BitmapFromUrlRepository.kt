package com.example.photoeditor.feature.main.domain.usecase.getbitmapfromurl

import android.graphics.Bitmap
import com.example.photoeditor.shared.domain.model.State
import io.reactivex.Observable

interface BitmapFromUrlRepository {
    fun getBitmapFromUrl(url: String): Observable<State<Bitmap>>
}