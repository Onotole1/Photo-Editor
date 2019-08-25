package com.spitchenko.photoeditor.feature.main.data.repository.getexif.datasource

import com.spitchenko.photoeditor.utils.exif
import com.spitchenko.photoeditor.utils.getAllAttributes
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class DiskExifDataSource @Inject constructor(
    @Named("controller_image_dir")
    private val imagesDir: File
) : ExifDataSource {

    override fun getExif(): Single<Map<String, String>> = Single.fromCallable {
        imagesDir.listFiles().first().exif().getAllAttributes()
    }.observeOn(Schedulers.io())
}