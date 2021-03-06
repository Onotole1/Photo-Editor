package com.spitchenko.photoeditor.feature.main.data.repository.getexif

import com.spitchenko.photoeditor.feature.main.data.repository.getexif.datasource.ExifDataSource
import com.spitchenko.photoeditor.feature.main.domain.usecase.getexif.GetExifRepository
import io.reactivex.Single
import javax.inject.Inject

class GetExifRepositoryImpl @Inject constructor(
    private val diskDataSource: ExifDataSource
): GetExifRepository {

    override fun getExif(): Single<Map<String, String>> = diskDataSource.getExif()
}