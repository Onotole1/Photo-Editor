package com.spitchenko.domain.usecase

import io.reactivex.observers.DisposableObserver

open class DefaultObserver<T>: DisposableObserver<T>() {
    override fun onComplete() {
        // do nothing
    }

    override fun onNext(value: T) {
        // do nothing
    }

    override fun onError(e: Throwable) {
        // do nothing
    }

}