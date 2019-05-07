package com.example.photoeditor.shared.domain.usecase

import io.reactivex.Completable
import io.reactivex.observers.DisposableCompletableObserver


abstract class UseCaseCompletable<Params> (
    executionThread: ExecutionThread,
    postThreadExecutionThread: ExecutionThread
): BaseUseCase(executionThread, postThreadExecutionThread) {

    protected abstract fun buildUseCaseObservable(params: Params): Completable

    fun execute(observer: DisposableCompletableObserver, params: Params) {
        val observable = buildUseCaseObservable(params)
            .subscribeOn(executionThread.scheduler)
            .observeOn(postThreadExecutionThread.scheduler)
        addDisposable(observable.subscribeWith(observer))
    }
}