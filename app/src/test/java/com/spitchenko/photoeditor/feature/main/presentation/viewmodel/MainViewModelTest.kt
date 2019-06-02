package com.spitchenko.photoeditor.feature.main.presentation.viewmodel

import android.graphics.Bitmap
import androidx.databinding.ObservableArrayList
import com.spitchenko.domain.model.State
import com.spitchenko.domain.usecase.ExecutionThread
import com.spitchenko.domain.usecase.UseCase
import com.spitchenko.domain.usecase.UseCaseCompletable
import com.spitchenko.domain.usecase.UseCaseSingle
import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId
import com.spitchenko.photoeditor.feature.main.domain.entity.SetImageRequest
import com.spitchenko.photoeditor.feature.main.domain.entity.UriWithId
import com.spitchenko.photoeditor.feature.main.presentation.viewmodel.bindings.ItemControllerBinding
import com.spitchenko.photoeditor.feature.main.presentation.viewmodel.bindings.ItemResultBinding
import com.spitchenko.presentation.viewmodel.EventsDispatcher
import com.spitchenko.presentation.viewmodel.binding.BindingClass
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private val testExecutionThread = object : ExecutionThread {
        override val scheduler: Scheduler = Schedulers.trampoline()
    }
    private lateinit var viewModel: MainViewModel

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
        override fun buildUseCaseObservable(params: BitmapWithId): Observable<State<Bitmap>> =
            Observable.empty()
    }

    private val removeResult = object : UseCaseCompletable<Long>(
        testExecutionThread, testExecutionThread
    ) {
        override fun buildUseCaseObservable(params: Long): Completable {
            return Completable.fromCallable {
                viewModel.bindingList.removeAll {
                    it.itemId == params
                }
            }
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

    @Before
    fun setup() {
        viewModel = MainViewModel(
            MainViewModelUseCases(
                getBitmapFromUri,
                transformUseCase,
                transformUseCase,
                transformUseCase,
                removeResult,
                setControllerImage,
                getExif,
                getResults
            ),

            EventsDispatcher(),

            ObservableArrayList<BindingClass>()
        )
    }

    @Test
    fun `Initially binding list contains empty controller item`() {
        assertTrue(viewModel.bindingList.size == 1)
        assertTrue((viewModel.bindingList[0] as ItemControllerBinding).image == null)
    }

    @Test
    fun `Remove result test`() {
        viewModel.bindingList.add(ItemResultBinding(435L))

        assertTrue(viewModel.bindingList.size == 2)

        viewModel.onImageClick(1)
        viewModel.removeImage()

        assertTrue(viewModel.bindingList.size == 1)
    }
}