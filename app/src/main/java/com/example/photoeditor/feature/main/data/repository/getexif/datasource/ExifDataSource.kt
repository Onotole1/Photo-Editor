package com.example.photoeditor.feature.main.data.repository.getexif.datasource

import io.reactivex.Observable

interface ExifDataSource {
    fun getExif(): Observable<Map<String, String>>
}