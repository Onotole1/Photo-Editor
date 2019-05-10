package com.example.photoeditor.feature.main.data.repository.getexif

import com.example.photoeditor.feature.main.data.repository.getexif.datasource.ExifDataSource
import com.example.photoeditor.feature.main.domain.usecase.getexif.GetExifRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetExifRepositoryImpl @Inject constructor(
    private val diskDataSource: ExifDataSource
): GetExifRepository {
    override fun getExif(): Observable<Map<String, String>> {
        return diskDataSource.getExif()
    }
}