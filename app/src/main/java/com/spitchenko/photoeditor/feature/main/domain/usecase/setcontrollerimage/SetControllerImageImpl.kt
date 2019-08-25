package com.spitchenko.photoeditor.feature.main.domain.usecase.setcontrollerimage

import com.spitchenko.photoeditor.feature.main.domain.entity.SetImageRequest
import io.reactivex.Completable
import javax.inject.Inject

class SetControllerImageImpl @Inject constructor(
    private val repository: SetControllerImageRepository
) : SetControllerImage {

    override fun invoke(request: SetImageRequest): Completable {
        return repository.setControllerImage(request)
    }
}