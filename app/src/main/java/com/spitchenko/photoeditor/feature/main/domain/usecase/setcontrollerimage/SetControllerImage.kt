package com.spitchenko.photoeditor.feature.main.domain.usecase.setcontrollerimage

import com.spitchenko.photoeditor.feature.main.domain.entity.SetImageRequest
import io.reactivex.Completable

interface SetControllerImage {

    operator fun invoke(request: SetImageRequest): Completable
}