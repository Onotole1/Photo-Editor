package com.example.photoeditor.feature.main.data.repository.setcontrollerimage

import com.example.photoeditor.feature.main.data.repository.setcontrollerimage.datasource.SetControllerImageSource
import com.example.photoeditor.feature.main.domain.entity.SetImageRequest
import com.example.photoeditor.feature.main.domain.usecase.setcontrollerimage.SetControllerImageRepository
import io.reactivex.Completable
import javax.inject.Inject

class SetControllerImageRepositoryImpl @Inject constructor(
    private val diskSetControllerImageSource: SetControllerImageSource
): SetControllerImageRepository {
    override fun setControllerImage(request: SetImageRequest): Completable {
        return diskSetControllerImageSource.setControllerImage(request)
    }
}