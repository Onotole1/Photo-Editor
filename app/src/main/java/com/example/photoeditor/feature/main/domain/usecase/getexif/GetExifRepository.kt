package com.example.photoeditor.feature.main.domain.usecase.getexif

import io.reactivex.Observable

interface GetExifRepository {
    fun getExif(): Observable<Map<String, String>>
}