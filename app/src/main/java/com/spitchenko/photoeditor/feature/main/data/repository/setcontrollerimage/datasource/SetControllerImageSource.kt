package com.spitchenko.photoeditor.feature.main.data.repository.setcontrollerimage.datasource

import com.spitchenko.photoeditor.feature.main.domain.entity.SetImageRequest
import io.reactivex.Completable

interface SetControllerImageSource {
    fun setControllerImage(request: SetImageRequest): Completable
}