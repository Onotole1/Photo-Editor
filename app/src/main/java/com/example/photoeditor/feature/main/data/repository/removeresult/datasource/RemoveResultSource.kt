package com.example.photoeditor.feature.main.data.repository.removeresult.datasource

import io.reactivex.Completable

interface RemoveResultSource {
    fun removeResult(imageId: Long): Completable
}