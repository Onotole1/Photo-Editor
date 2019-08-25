package com.spitchenko.photoeditor.feature.main.data.repository.removeresult

import com.spitchenko.photoeditor.feature.main.data.repository.removeresult.datasource.RemoveResultSource
import com.spitchenko.photoeditor.feature.main.domain.usecase.removeresult.RemoveResultRepository
import io.reactivex.Completable
import javax.inject.Inject

class RemoveResultRepositoryImpl @Inject constructor(
    private val diskRemoveResultSource: RemoveResultSource
) : RemoveResultRepository {

    override fun removeResult(imageId: Long): Completable =
        diskRemoveResultSource.removeResult(imageId)
}