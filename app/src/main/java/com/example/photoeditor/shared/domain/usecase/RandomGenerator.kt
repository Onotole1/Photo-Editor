package com.example.photoeditor.shared.domain.usecase

interface RandomGenerator<T> {
    fun generate(): T
}