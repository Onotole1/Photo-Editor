package com.example.photoeditor.shared.domain.usecase

import io.reactivex.observers.DisposableCompletableObserver

abstract class DefaultCompletableObserver: DisposableCompletableObserver() {
    override fun onComplete() {
        // do nothing
    }

    override fun onStart() {
        // do nothing
    }

    override fun onError(e: Throwable) {
        // do nothing
    }

}