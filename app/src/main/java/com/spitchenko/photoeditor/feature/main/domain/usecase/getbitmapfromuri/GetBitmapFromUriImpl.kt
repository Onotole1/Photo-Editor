package com.spitchenko.photoeditor.feature.main.domain.usecase.getbitmapfromuri

import android.graphics.Bitmap
import com.spitchenko.domain.model.State
import com.spitchenko.photoeditor.feature.main.domain.entity.UriWithId
import io.reactivex.Observable
import javax.inject.Inject

class GetBitmapFromUriImpl @Inject constructor(
    private val repository: BitmapFromUriRepository
) : GetBitmapFromUri {

    override fun invoke(params: UriWithId): Observable<State<Bitmap>> {
        return repository.getBitmapFromUri(params)
    }
}