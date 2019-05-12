package com.spitchenko.photoeditor.feature.main.data.repository.setcontrollerimage

import com.spitchenko.photoeditor.feature.main.data.repository.setcontrollerimage.datasource.SetControllerImageSource
import com.spitchenko.photoeditor.feature.main.domain.entity.SetImageRequest
import com.spitchenko.photoeditor.feature.main.domain.usecase.setcontrollerimage.SetControllerImageRepository
import io.reactivex.Completable
import javax.inject.Inject

class SetControllerImageRepositoryImpl @Inject constructor(
    private val diskSetControllerImageSource: SetControllerImageSource
): SetControllerImageRepository {
    override fun setControllerImage(request: SetImageRequest): Completable {
        return diskSetControllerImageSource.setControllerImage(request)
    }
}