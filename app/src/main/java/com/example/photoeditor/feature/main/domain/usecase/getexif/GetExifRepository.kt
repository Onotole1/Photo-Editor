package com.example.photoeditor.feature.main.domain.usecase.getexif

import io.reactivex.Single

interface GetExifRepository {
    fun getExif(): Single<Map<String, String>>
}