package com.spitchenko.photoeditor.feature.main.domain.usecase.getexif

import io.reactivex.Single
import javax.inject.Inject

class GetExifImpl @Inject constructor(
    private val repository: GetExifRepository
) : GetExif {

    override fun invoke(): Single<Map<String, String>> {
        return repository.getExif()
    }
}