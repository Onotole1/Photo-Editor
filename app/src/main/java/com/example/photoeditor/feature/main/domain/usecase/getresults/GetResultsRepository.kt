package com.example.photoeditor.feature.main.domain.usecase.getresults

import com.example.photoeditor.feature.main.domain.entity.BitmapWithId
import io.reactivex.Observable

interface GetResultsRepository {
    fun getResults(): Observable<List<BitmapWithId>>
}