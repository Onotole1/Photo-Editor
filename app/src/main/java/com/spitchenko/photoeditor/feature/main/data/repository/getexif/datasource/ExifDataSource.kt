package com.spitchenko.photoeditor.feature.main.data.repository.getexif.datasource

import io.reactivex.Single

interface ExifDataSource {
    fun getExif(): Single<Map<String, String>>
}