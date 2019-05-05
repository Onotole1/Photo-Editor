package com.example.photoeditor.shared.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.photoeditor.shared.domain.usecase.BaseUseCase

abstract class BaseViewModel(vararg useCases: BaseUseCase): ViewModel() {
    private val useCaseContainer = useCases

    override fun onCleared() {
        super.onCleared()

        useCaseContainer.forEach {
            it.dispose()
        }
    }
}