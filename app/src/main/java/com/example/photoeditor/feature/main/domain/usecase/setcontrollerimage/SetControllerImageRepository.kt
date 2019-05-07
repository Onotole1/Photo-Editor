package com.example.photoeditor.feature.main.domain.usecase.setcontrollerimage

import com.example.photoeditor.feature.main.domain.entity.SetImageRequest
import io.reactivex.Completable

interface SetControllerImageRepository {
    fun setControllerImage(request: SetImageRequest): Completable
}