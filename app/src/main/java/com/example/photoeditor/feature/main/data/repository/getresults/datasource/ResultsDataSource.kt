package com.example.photoeditor.feature.main.data.repository.getresults.datasource

import com.example.photoeditor.feature.main.domain.entity.BitmapWithId
import io.reactivex.Observable

interface ResultsDataSource {
    fun getResults(): Observable<List<BitmapWithId>>
}