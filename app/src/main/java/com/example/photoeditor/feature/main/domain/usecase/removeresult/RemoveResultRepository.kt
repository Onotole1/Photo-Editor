package com.example.photoeditor.feature.main.domain.usecase.removeresult

import io.reactivex.Completable

interface RemoveResultRepository {
    fun removeResult(imageId: Long): Completable
}