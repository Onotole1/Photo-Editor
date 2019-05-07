package com.example.photoeditor.feature.main.data.repository.setcontrollerimage.datasource

import com.example.photoeditor.feature.main.domain.entity.SetImageRequest
import io.reactivex.Completable

interface SetControllerImageSource {
    fun setControllerImage(request: SetImageRequest): Completable
}