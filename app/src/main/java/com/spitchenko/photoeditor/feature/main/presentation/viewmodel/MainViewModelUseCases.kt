package com.spitchenko.photoeditor.feature.main.presentation.viewmodel

import com.spitchenko.photoeditor.feature.main.domain.usecase.getbitmapfromuri.GetBitmapFromUri
import com.spitchenko.photoeditor.feature.main.domain.usecase.getexif.GetExif
import com.spitchenko.photoeditor.feature.main.domain.usecase.getresults.GetResults
import com.spitchenko.photoeditor.feature.main.domain.usecase.removeresult.RemoveResult
import com.spitchenko.photoeditor.feature.main.domain.usecase.setcontrollerimage.SetControllerImage
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.invertbitmap.InvertBitmap
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.mirrorbitmap.MirrorBitmap
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.rotatebitmap.RotateBitmap

class MainViewModelUseCases(
    val getBitmapFromUri: GetBitmapFromUri,
    val rotateBitmap: RotateBitmap,
    val mirrorBitmap: MirrorBitmap,
    val invertBitmap: InvertBitmap,
    val removeResult: RemoveResult,
    val setControllerImage: SetControllerImage,
    val getExif: GetExif,
    val getResults: GetResults
)