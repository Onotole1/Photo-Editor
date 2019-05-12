package com.spitchenko.photoeditor.feature.main.data.repository.getresults.datasource

import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId
import io.reactivex.Single

interface ResultsDataSource {
    fun getResults(): Single<List<BitmapWithId>>
}