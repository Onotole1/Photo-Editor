package com.example.photoeditor.shared.domain.model

sealed class State<T> {
    class Data<T>(val data: T) : State<T>()
    class Progress<T>(val progress: Int?) : State<T>()
}