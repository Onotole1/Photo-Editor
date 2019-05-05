package com.example.photoeditor.feature.main.data.repository.getresults

import com.example.photoeditor.feature.main.data.repository.getresults.datasource.ResultsDataSource
import com.example.photoeditor.feature.main.domain.entity.BitmapWithId
import com.example.photoeditor.feature.main.domain.usecase.getresults.GetResultsRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetresultsRepositoryImpl @Inject constructor(
    private val diskResultsDataSource: ResultsDataSource
) : GetResultsRepository {
    override fun getResults(): Observable<List<BitmapWithId>> {
        return diskResultsDataSource.getResults()
    }
}