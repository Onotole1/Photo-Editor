package com.spitchenko.photoeditor.feature.main.data.repository.getresults

import com.spitchenko.photoeditor.feature.main.data.repository.getresults.datasource.ResultsDataSource
import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId
import com.spitchenko.photoeditor.feature.main.domain.usecase.getresults.GetResultsRepository
import io.reactivex.Single
import javax.inject.Inject

class GetresultsRepositoryImpl @Inject constructor(
    private val diskResultsDataSource: ResultsDataSource
) : GetResultsRepository {
    override fun getResults(): Single<List<BitmapWithId>> {
        return diskResultsDataSource.getResults()
    }
}