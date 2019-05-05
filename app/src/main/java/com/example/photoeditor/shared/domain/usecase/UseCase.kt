package com.example.photoeditor.shared.domain.usecase

import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver


abstract class UseCase<T, Params> (
    executionThread: ExecutionThread,
    postThreadExecutionThread: ExecutionThread
): BaseUseCase(executionThread, postThreadExecutionThread) {
    abstract fun buildUseCaseObservable(params: Params): Observable<T>

    fun execute(observer: DisposableObserver<T>, params: Params) {
        val observable = buildUseCaseObservable(params)
            .subscribeOn(executionThread.scheduler)
            .observeOn(postThreadExecutionThread.scheduler)
        addDisposable(observable.subscribeWith(observer))
    }
}