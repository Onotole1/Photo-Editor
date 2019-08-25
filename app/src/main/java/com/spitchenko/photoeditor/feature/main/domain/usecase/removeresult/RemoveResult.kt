package com.spitchenko.photoeditor.feature.main.domain.usecase.removeresult

import io.reactivex.Completable

interface RemoveResult {

    operator fun invoke(resultId: Long): Completable
}