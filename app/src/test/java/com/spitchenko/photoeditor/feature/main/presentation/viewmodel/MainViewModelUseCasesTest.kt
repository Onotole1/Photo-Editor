package com.spitchenko.photoeditor.feature.main.presentation.viewmodel

import android.graphics.Bitmap
import com.spitchenko.domain.model.State
import com.spitchenko.domain.usecase.UseCase
import com.spitchenko.domain.usecase.UseCaseCompletable
import com.spitchenko.domain.usecase.UseCaseSingle
import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId
import com.spitchenko.photoeditor.feature.main.domain.entity.SetImageRequest
import com.spitchenko.photoeditor.feature.main.domain.entity.UriWithId
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelUseCasesTest {

    @Mock
    lateinit var transform: UseCase<State<Bitmap>, BitmapWithId>

    @Mock
    lateinit var getBitmapFromUri: UseCase<State<Bitmap>, UriWithId>

    @Mock
    lateinit var removeResult: UseCaseCompletable<Long>

    @Mock
    lateinit var setImage: UseCaseCompletable<SetImageRequest>

    @Mock
    lateinit var getExif: UseCaseSingle<Map<String, String>, Unit>

    @Mock
    lateinit var getResults: UseCaseSingle<List<BitmapWithId>, Unit>

    @Test
    fun `Check all use cases in list`() {

        val useCases = MainViewModelUseCases(
            getBitmapFromUri,
            transform,
            transform,
            transform,
            removeResult,
            setImage,
            getExif,
            getResults
        )

        assertEquals(MainViewModelUseCases::class.java.constructors.first().parameterCount, useCases.size)
    }
}