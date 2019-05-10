package com.example.photoeditor.feature.main.data.repository.getbitmapfromuri.datasource

import android.graphics.Bitmap
import com.example.photoeditor.feature.main.domain.entity.UriWithId
import com.example.photoeditor.shared.domain.model.State
import io.reactivex.Observable

interface BitmapDataSource {
    fun getBitmapFromUrl(uriWithId: UriWithId): Observable<State<Bitmap>>
}