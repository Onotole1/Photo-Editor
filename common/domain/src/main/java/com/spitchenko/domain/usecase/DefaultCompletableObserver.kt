package com.spitchenko.domain.usecase

import io.reactivex.observers.DisposableCompletableObserver

open class DefaultCompletableObserver: DisposableCompletableObserver() {
    override fun onComplete() {
        // do nothing
    }

    override fun onError(e: Throwable) {
        // do nothing
    }

}