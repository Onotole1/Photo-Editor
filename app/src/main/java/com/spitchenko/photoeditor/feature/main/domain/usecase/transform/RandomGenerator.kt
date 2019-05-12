package com.spitchenko.photoeditor.feature.main.domain.usecase.transform

interface RandomGenerator<T> {
    fun generate(): T
}