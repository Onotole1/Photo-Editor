package com.spitchenko.presentation.viewmodel

import com.spitchenko.domain.usecase.DefaultCompletableObserver
import com.spitchenko.domain.usecase.ExecutionThread
import com.spitchenko.domain.usecase.UseCaseCompletable
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.TimeUnit

class BaseViewModelTest {

    private val testExecutionThread = TestExecutionThread()

    var clear: (() -> Unit)? = null

    var disposable: Disposable? = null

    val testUseCase = object : UseCaseCompletable<Unit>(testExecutionThread, testExecutionThread) {

        override fun buildUseCaseObservable(params: Unit): Completable {
            return Completable.timer(1000, TimeUnit.SECONDS).doOnSubscribe {
                disposable = it
            }
        }
    }

    private fun testViewModel() = object : BaseViewModel(testUseCase) {
        init {
            clear = {
                onCleared()
            }
        }
    }

    @Test
    fun `Use case is disposed after viewModel cleared`() {

        testViewModel()

        assertNull(disposable)

        testUseCase.execute(DefaultCompletableObserver(), Unit)

        assertTrue(disposable?.isDisposed == false)

        clear?.invoke()

        assertTrue(disposable?.isDisposed == true)
    }

    class TestExecutionThread : ExecutionThread {
        override val scheduler: Scheduler = Schedulers.trampoline()
    }
}