package com.spitchenko.photoeditor.utils

import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableContainer

fun Disposable.addTo(container: DisposableContainer) {
    container.add(this)
}