package com.spitchenko.domain.usecase

import io.reactivex.Single
import io.reactivex.observers.DisposableSingleObserver


abstract class UseCaseSingle<T, Params> (
    executionThread: ExecutionThread,
    postThreadExecutionThread: ExecutionThread
): BaseUseCase(executionThread, postThreadExecutionThread) {
    protected abstract fun buildUseCaseObservable(params: Params): Single<T>

    fun execute(observer: DisposableSingleObserver<T>, params: Params) {
        val observable = buildUseCaseObservable(params)
            .subscribeOn(executionThread.scheduler)
            .observeOn(postThreadExecutionThread.scheduler)
        addDisposable(observable.subscribeWith(observer))
    }
}