package com.example.photoeditor.feature.main.data.repository.getresults.datasource

import com.example.photoeditor.feature.main.domain.entity.BitmapWithId
import io.reactivex.Single

interface ResultsDataSource {
    fun getResults(): Single<List<BitmapWithId>>
}