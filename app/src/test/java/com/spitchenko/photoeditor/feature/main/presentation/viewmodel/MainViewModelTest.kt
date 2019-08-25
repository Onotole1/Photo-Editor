package com.spitchenko.photoeditor.feature.main.presentation.viewmodel

import androidx.databinding.ObservableArrayList
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.spitchenko.photoeditor.feature.main.domain.usecase.getbitmapfromuri.GetBitmapFromUri
import com.spitchenko.photoeditor.feature.main.domain.usecase.getexif.GetExif
import com.spitchenko.photoeditor.feature.main.domain.usecase.getresults.GetResults
import com.spitchenko.photoeditor.feature.main.domain.usecase.removeresult.RemoveResult
import com.spitchenko.photoeditor.feature.main.domain.usecase.setcontrollerimage.SetControllerImage
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.invertbitmap.InvertBitmap
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.mirrorbitmap.MirrorBitmap
import com.spitchenko.photoeditor.feature.main.domain.usecase.transform.rotatebitmap.RotateBitmap
import com.spitchenko.photoeditor.feature.main.presentation.viewmodel.bindings.ItemControllerBinding
import com.spitchenko.photoeditor.feature.main.presentation.viewmodel.bindings.ItemResultBinding
import com.spitchenko.presentation.viewmodel.EventsDispatcher
import com.spitchenko.test.RxSchedulersTestRule
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MainViewModelTest {

    @get:Rule
    var rxSchedulersTestRule: TestRule = RxSchedulersTestRule()

    private lateinit var viewModel: MainViewModel

    private val getBitmapFromUri: GetBitmapFromUri = mock()
    private val rotateBitmap: RotateBitmap = mock()
    private val mirrorBitmap: MirrorBitmap = mock()
    private val invertBitmap: InvertBitmap = mock()
    private val removeResult: RemoveResult = mock()
    private val setControllerImage: SetControllerImage = mock()
    private val getExif: GetExif = mock()
    private val getResults: GetResults = mock()

    @Before
    fun setup() {
        viewModel = MainViewModel(
            MainViewModelUseCases(
                getBitmapFromUri,
                rotateBitmap,
                mirrorBitmap,
                invertBitmap,
                removeResult,
                setControllerImage,
                getExif,
                getResults
            ),

            EventsDispatcher(),

            ObservableArrayList()
        )
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(
            getBitmapFromUri,
            rotateBitmap,
            mirrorBitmap,
            invertBitmap,
            removeResult,
            setControllerImage,
            getExif,
            getResults
        )
    }

    @Test
    fun `Initially binding list contains empty controller item`() {
        whenever(getResults()).thenReturn(Single.just(emptyList()))

        viewModel.init()

        assertThat(viewModel.bindingList.size).isEqualTo(1)
        assertThat((viewModel.bindingList[0] as ItemControllerBinding).image).isNull()
        verify(getResults).invoke()
    }

    @Test
    fun `Remove result test`() {
        whenever(removeResult(TEST_ITEM_ID)).thenReturn(Completable.complete())

        viewModel.bindingList.add(ItemResultBinding(TEST_ITEM_ID))

        assertThat(viewModel.bindingList.size).isEqualTo(1)

        viewModel.onImageClick(0)
        viewModel.removeImage()

        assertThat(viewModel.bindingList.isEmpty()).isTrue()

        verify(removeResult).invoke(TEST_ITEM_ID)
    }

    private companion object {
        const val TEST_ITEM_ID = 435L
    }
}