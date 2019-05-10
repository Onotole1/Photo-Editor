package com.example.photoeditor.feature.main.data.repository.getexif.datasource

import com.example.photoeditor.utils.exif
import com.example.photoeditor.utils.getAllAttributes
import io.reactivex.Observable
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class DiskExifDataSource @Inject constructor(
    @Named("controller_image_dir")
    private val imagesDir: File
) : ExifDataSource {
    override fun getExif(): Observable<Map<String, String>> {
        return Observable.fromCallable {
            imagesDir.listFiles().first().exif().getAllAttributes()
        }
    }
}