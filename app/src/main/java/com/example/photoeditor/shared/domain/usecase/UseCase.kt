package com.example.photoeditor.shared.domain.usecase

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver


abstract class UseCase<T, Params> (
    private val executionThread: ExecutionThread,
    private val postThreadExecutionThread: ExecutionThread
) {
    private val disposables: CompositeDisposable = CompositeDisposable()

    abstract fun buildUseCaseObservable(params: Params): Observable<T>

    fun execute(observer: DisposableObserver<T>, params: Params) {
        val observable = buildUseCaseObservable(params)
            .subscribeOn(executionThread.scheduler)
            .observeOn(postThreadExecutionThread.scheduler)
        addDisposable(observable.subscribeWith(observer))
    }

    fun dispose() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }

    private fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }
}