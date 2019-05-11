package com.example.photoeditor.shared.domain.usecase

import io.reactivex.observers.DisposableSingleObserver

open class DefaultSingleObserver<T>: DisposableSingleObserver<T>() {
    override fun onSuccess(value: T) {
        // do nothing
    }

    override fun onError(e: Throwable) {
        // do nothing
    }

}