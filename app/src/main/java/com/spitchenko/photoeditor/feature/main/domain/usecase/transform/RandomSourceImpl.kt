package com.spitchenko.photoeditor.feature.main.domain.usecase.transform

import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RandomSourceImpl @Inject constructor() : RandomSource {
    override fun invoke(random: Long): Observable<Long> =
        Observable.interval(1, TimeUnit.SECONDS).take(random)
}