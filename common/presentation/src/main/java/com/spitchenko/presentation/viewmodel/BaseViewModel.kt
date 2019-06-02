package com.spitchenko.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.spitchenko.domain.usecase.BaseUseCase

abstract class BaseViewModel(private val useCases: Iterable<BaseUseCase>) : ViewModel() {
    constructor(vararg useCases: BaseUseCase) : this(useCases.asIterable())

    override fun onCleared() {
        super.onCleared()

        useCases.forEach {
            it.dispose()
        }
    }
}