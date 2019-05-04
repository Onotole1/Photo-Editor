package com.example.photoeditor.feature.main.data.repository.getbitmapfromurl.datasource

import android.graphics.Bitmap
import com.example.photoeditor.shared.domain.model.State
import io.reactivex.Observable
import java.net.URL

interface BitmapDataSource {
    fun getBitmapFromUrl(url: String): Observable<State<Bitmap>>
}