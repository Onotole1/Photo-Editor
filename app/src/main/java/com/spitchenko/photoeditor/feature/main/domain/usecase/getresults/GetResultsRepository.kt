package com.spitchenko.photoeditor.feature.main.domain.usecase.getresults

import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId
import io.reactivex.Single

interface GetResultsRepository {
    fun getResults(): Single<List<BitmapWithId>>
}