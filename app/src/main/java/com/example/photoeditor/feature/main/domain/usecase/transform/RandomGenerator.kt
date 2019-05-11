package com.example.photoeditor.feature.main.domain.usecase.transform

interface RandomGenerator<T> {
    fun generate(): T
}