package com.example.photoeditor.feature.main.data.repository.getbitmapfromurl

import android.graphics.Bitmap
import com.example.photoeditor.feature.main.data.repository.getbitmapfromurl.datasource.BitmapDataSource
import com.example.photoeditor.feature.main.domain.usecase.getbitmapfromurl.BitmapFromUrlRepository
import com.example.photoeditor.shared.domain.model.State
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import javax.inject.Inject
import javax.inject.Named

class BitmapFromUrlRepositoryImpl @Inject constructor(
    @Named("network_bitmap_data_source")
    private val networkBitmapDataSource: BitmapDataSource,
    @Named("disk_bitmap_data_source")
    private val diskBitmapDataSource: BitmapDataSource
) : BitmapFromUrlRepository {
    override fun getBitmapFromUrl(url: String): Observable<State<Bitmap>> {
        return networkBitmapDataSource.getBitmapFromUrl(url)
            .onErrorResumeNext(Function<Throwable, ObservableSource<State<Bitmap>>> { t ->
                diskBitmapDataSource.getBitmapFromUrl(url).onErrorResumeNext(ObservableSource {
                    it.onError(t)
                })
            })
    }
}