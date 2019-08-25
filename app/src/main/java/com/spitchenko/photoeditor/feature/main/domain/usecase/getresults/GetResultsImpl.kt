package com.spitchenko.photoeditor.feature.main.domain.usecase.getresults

import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId
import io.reactivex.Single
import javax.inject.Inject

class GetResultsImpl @Inject constructor(
    private val repository: GetResultsRepository
) : GetResults {

    override fun invoke(): Single<List<BitmapWithId>> {
        return repository.getResults()
    }
}