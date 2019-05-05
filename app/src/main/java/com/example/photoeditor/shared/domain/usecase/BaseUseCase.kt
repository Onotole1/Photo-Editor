package com.example.photoeditor.shared.domain.usecase

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseUseCase(
    protected val executionThread: ExecutionThread,
    protected val postThreadExecutionThread: ExecutionThread
) {
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun dispose() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }

    protected fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }
}