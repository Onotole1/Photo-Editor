package com.spitchenko.photoeditor.feature.main.presentation.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.databinding.ObservableArrayList
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.spitchenko.domain.model.State
import com.spitchenko.photoeditor.feature.main.domain.entity.SetImageRequest
import com.spitchenko.photoeditor.feature.main.domain.entity.UriWithId
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
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MainViewModelInstrumentedTest {

    @get:Rule
    var rxSchedulersTestRule: TestRule = RxSchedulersTestRule()

    private val getBitmapFromUri: GetBitmapFromUri = mock()
    private val rotateBitmap: RotateBitmap = mock()
    private val mirrorBitmap: MirrorBitmap = mock()
    private val invertBitmap: InvertBitmap = mock()
    private val removeResult: RemoveResult = mock()
    private val setControllerImage: SetControllerImage = mock()
    private val getExif: GetExif = mock()
    private val getResults: GetResults = mock()

    private val testBitmap = Bitmap.createBitmap(100, 50, Bitmap.Config.ARGB_8888)

    private val testDispatcher = EventsDispatcher<MainViewModel.EventsListener>()

    @Test
    fun transform_use_case_test() {

        whenever(rotateBitmap(any())).thenReturn(Observable.just(State.Data(testBitmap)))
        whenever(getResults()).thenReturn(Single.just(emptyList()))

        val viewModel = MainViewModel(
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

            testDispatcher,

            ObservableArrayList()
        )

        val inputBitmap = Bitmap.createBitmap(50, 100, Bitmap.Config.ARGB_8888)

        viewModel.init()
        viewModel.onRotateClick(inputBitmap)

        assertThat((viewModel.bindingList[1] as ItemResultBinding).image?.sameAs(testBitmap)).isTrue()

        verify(rotateBitmap).invoke(any())
        verify(getResults).invoke()
    }

    @Test
    fun get_image_from_uri_use_case_test() {
        whenever(getResults()).thenReturn(Single.just(emptyList()))

        val testUri = Uri.EMPTY

        val getBitmapFromUri = object : GetBitmapFromUri {
            override fun invoke(params: UriWithId): Observable<State<Bitmap>> {
                assertThat(params.uri).isEqualTo(testUri)
                return Observable.just(State.Data(testBitmap))
            }
        }

        val viewModel = MainViewModel(
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

            testDispatcher,

            ObservableArrayList()
        )

        viewModel.init()

        viewModel.setImage(testUri)

        assertThat((viewModel.bindingList[0] as ItemControllerBinding).image?.sameAs(testBitmap)).isTrue()

        viewModel.downloadImageByUrl(testUri.toString())

        assertThat((viewModel.bindingList[0] as ItemControllerBinding).image?.sameAs(testBitmap)).isTrue()

        verify(getResults).invoke()
    }

    @Test
    fun replace_existing_image_use_case_test() {
        val testResultId = 1L

        whenever(getResults()).thenReturn(Single.just(emptyList()))
        whenever(
            setControllerImage(
                SetImageRequest(
                    testResultId,
                    0
                )
            )
        ).thenReturn(Completable.complete())

        val viewModel = MainViewModel(
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

            testDispatcher,

            ObservableArrayList()
        )

        viewModel.init()

        viewModel.bindingList.add(ItemResultBinding(testResultId, testBitmap))

        viewModel.onImageClick(1)

        viewModel.replaceExistingImage()

        verify(getResults).invoke()
        verify(setControllerImage).invoke(SetImageRequest(testResultId, 0))
    }
}