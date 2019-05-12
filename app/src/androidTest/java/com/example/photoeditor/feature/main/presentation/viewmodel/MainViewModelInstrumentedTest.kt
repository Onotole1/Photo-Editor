package com.example.photoeditor.feature.main.presentation.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import com.example.photoeditor.feature.main.domain.entity.BitmapWithId
import com.example.photoeditor.feature.main.domain.entity.SetImageRequest
import com.example.photoeditor.feature.main.domain.entity.UriWithId
import com.example.photoeditor.feature.main.presentation.viewmodel.bindings.ItemControllerBinding
import com.example.photoeditor.feature.main.presentation.viewmodel.bindings.ItemResultBinding
import com.spitchenko.domain.model.State
import com.spitchenko.domain.usecase.ExecutionThread
import com.spitchenko.domain.usecase.UseCase
import com.spitchenko.domain.usecase.UseCaseCompletable
import com.spitchenko.domain.usecase.UseCaseSingle
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.*
import org.junit.Test

class MainViewModelInstrumentedTest {

    private val testExecutionThread = object : ExecutionThread {
        override val scheduler: Scheduler = Schedulers.trampoline()
    }

    private val getBitmapFromUri = object : UseCase<State<Bitmap>, UriWithId>(
        testExecutionThread,
        testExecutionThread
    ) {
        override fun buildUseCaseObservable(params: UriWithId): Observable<State<Bitmap>> =
            Observable.empty()
    }

    private val transformUseCase = object : UseCase<State<Bitmap>, BitmapWithId>(
        testExecutionThread,
        testExecutionThread
    ) {
        override fun buildUseCaseObservable(params: BitmapWithId): Observable<State<Bitmap>> {
            return Observable.empty()
        }

    }

    private val removeResult = object : UseCaseCompletable<Long>(
        testExecutionThread, testExecutionThread
    ) {
        override fun buildUseCaseObservable(params: Long): Completable {
            return Completable.complete()
        }
    }

    private val setControllerImage = object : UseCaseCompletable<SetImageRequest>(
        testExecutionThread, testExecutionThread
    ) {
        override fun buildUseCaseObservable(params: SetImageRequest): Completable {
            return Completable.complete()
        }
    }

    private val getExif = object : UseCaseSingle<Map<String, String>, Unit>(
        testExecutionThread, testExecutionThread
    ) {
        override fun buildUseCaseObservable(params: Unit): Single<Map<String, String>> {
            return Single.fromObservable(Observable.empty())
        }
    }

    private val getResults = object : UseCaseSingle<List<BitmapWithId>, Unit>(
        testExecutionThread, testExecutionThread
    ) {
        override fun buildUseCaseObservable(params: Unit): Single<List<BitmapWithId>> {
            return Single.fromObservable(Observable.empty())
        }
    }

    private val testBitmap = Bitmap.createBitmap(100, 50, Bitmap.Config.ARGB_8888)

    @Test
    fun transform_use_case_test() {

        val transformUseCase = object : UseCase<State<Bitmap>, BitmapWithId>(
            testExecutionThread,
            testExecutionThread
        ) {
            override fun buildUseCaseObservable(params: BitmapWithId): Observable<State<Bitmap>> =
                Observable.just(State.Data(testBitmap))
        }

        val viewModel = MainViewModel(
            getBitmapFromUri,
            transformUseCase,
            transformUseCase,
            transformUseCase,
            removeResult,
            setControllerImage,
            getExif,
            getResults
        )

        val inputBitmap = Bitmap.createBitmap(50, 100, Bitmap.Config.ARGB_8888)

        viewModel.onRotateClick(inputBitmap)

        assertTrue((viewModel.bindingList[1] as ItemResultBinding).image?.sameAs(testBitmap) == true)
    }

    @Test
    fun get_image_from_uri_use_case_test() {

        val testUri = Uri.EMPTY

        val getBitmapFromUri = object : UseCase<State<Bitmap>, UriWithId>(
            testExecutionThread,
            testExecutionThread
        ) {
            override fun buildUseCaseObservable(params: UriWithId): Observable<State<Bitmap>> {
                assertTrue(params.uri == testUri)
                return Observable.just(State.Data(testBitmap))
            }
        }

        val viewModel = MainViewModel(
            getBitmapFromUri,
            transformUseCase,
            transformUseCase,
            transformUseCase,
            removeResult,
            setControllerImage,
            getExif,
            getResults
        )

        viewModel.setImage(testUri)

        assertTrue((viewModel.bindingList[0] as ItemControllerBinding).image?.sameAs(testBitmap) == true)

        viewModel.downloadImageByUrl(testUri.toString())

        assertTrue((viewModel.bindingList[0] as ItemControllerBinding).image?.sameAs(testBitmap) == true)
    }

    @Test
    fun replace_existing_image_use_case_test() {

        val viewModel = MainViewModel(
            getBitmapFromUri,
            transformUseCase,
            transformUseCase,
            transformUseCase,
            removeResult,
            setControllerImage,
            getExif,
            getResults
        )

        val testResultId = 1L

        viewModel.bindingList.add(ItemResultBinding(testResultId, testBitmap))

        viewModel.onImageClick(1)

        viewModel.replaceExistingImage()

        assertTrue((viewModel.bindingList[0] as ItemControllerBinding).image?.sameAs(testBitmap) == true)
    }
}