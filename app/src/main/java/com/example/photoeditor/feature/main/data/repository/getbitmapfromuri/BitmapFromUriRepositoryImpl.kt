package com.example.photoeditor.feature.main.data.repository.getbitmapfromuri

import android.graphics.Bitmap
import android.webkit.URLUtil
import com.example.photoeditor.feature.main.data.repository.getbitmapfromuri.datasource.BitmapDataSource
import com.example.photoeditor.feature.main.domain.entity.UriWithId
import com.example.photoeditor.feature.main.domain.usecase.getbitmapfromuri.BitmapFromUriRepository
import com.example.photoeditor.shared.domain.model.State
import io.reactivex.Completable
import io.reactivex.Observable
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class BitmapFromUriRepositoryImpl @Inject constructor(
    @Named("disk_bitmap_data_source")
    private val diskBitmapDataSource: BitmapDataSource,
    @Named("network_bitmap_data_source")
    private val networkBitmapDataSource: BitmapDataSource,
    @Named("controller_image_dir")
    private val imagesPath: File
) : BitmapFromUriRepository {

    override fun getBitmapFromUri(uri: UriWithId): Observable<State<Bitmap>> {

        val source = if (URLUtil.isNetworkUrl(uri.uri.toString())) {
            networkBitmapDataSource.getBitmapFromUrl(uri)
        } else {
            diskBitmapDataSource.getBitmapFromUrl(uri)
        }

        return Completable.fromCallable {
            imagesPath.listFiles()?.forEach {
                it.delete()
            }
        }.andThen(source)
    }
}