package com.spitchenko.photoeditor.feature.main.domain.usecase.getexif

import io.reactivex.Single

interface GetExif {

    operator fun invoke(): Single<Map<String, String>>
}