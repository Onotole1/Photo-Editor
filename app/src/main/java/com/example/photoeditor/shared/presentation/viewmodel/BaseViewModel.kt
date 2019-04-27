package com.example.photoeditor.shared.presentation.viewmodel

import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel {

    protected val compositeDisposable = CompositeDisposable()

    open fun onCleared() {
        compositeDisposable.dispose()
    }
}