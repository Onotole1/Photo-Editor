package com.spitchenko.photoeditor.feature.main.presentation.viewmodel

import android.graphics.Bitmap
import com.spitchenko.domain.model.State
import com.spitchenko.domain.usecase.BaseUseCase
import com.spitchenko.domain.usecase.UseCase
import com.spitchenko.domain.usecase.UseCaseCompletable
import com.spitchenko.domain.usecase.UseCaseSingle
import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId
import com.spitchenko.photoeditor.feature.main.domain.entity.SetImageRequest
import com.spitchenko.photoeditor.feature.main.domain.entity.UriWithId

class MainViewModelUseCases(
    val getBitmapFromUri: UseCase<State<Bitmap>, UriWithId>,
    val rotateBitmap: UseCase<State<Bitmap>, BitmapWithId>,
    val mirrorBitmap: UseCase<State<Bitmap>, BitmapWithId>,
    val invertBitmap: UseCase<State<Bitmap>, BitmapWithId>,
    val removeResult: UseCaseCompletable<Long>,
    val setControllerImage: UseCaseCompletable<SetImageRequest>,
    val getExif: UseCaseSingle<Map<String, String>, Unit>,
    val getResults: UseCaseSingle<List<BitmapWithId>, Unit>
) : List<BaseUseCase> by listOf(
    getBitmapFromUri,
    rotateBitmap,
    mirrorBitmap,
    invertBitmap,
    removeResult,
    setControllerImage,
    getExif,
    getResults
)