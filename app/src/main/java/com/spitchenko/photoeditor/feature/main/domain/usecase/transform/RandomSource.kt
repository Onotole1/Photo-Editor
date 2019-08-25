package com.spitchenko.photoeditor.feature.main.domain.usecase.transform

import io.reactivex.Observable

interface RandomSource {
    operator fun invoke(random: Long): Observable<Long>
}