package com.spitchenko.photoeditor.feature.main.domain.usecase.removeresult

import io.reactivex.Completable
import javax.inject.Inject

class RemoveResultImpl @Inject constructor(
    private val repository: RemoveResultRepository
) : RemoveResult {

    override fun invoke(resultId: Long): Completable {
        return repository.removeResult(resultId)
    }
}