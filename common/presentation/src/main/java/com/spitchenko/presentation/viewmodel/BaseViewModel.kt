package com.spitchenko.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.spitchenko.domain.usecase.BaseUseCase

abstract class BaseViewModel(vararg useCases: BaseUseCase): ViewModel() {
    private val useCaseContainer = useCases

    override fun onCleared() {
        super.onCleared()

        useCaseContainer.forEach {
            it.dispose()
        }
    }
}