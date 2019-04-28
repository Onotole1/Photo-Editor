package com.example.photoeditor.shared.presentation.viewmodel

interface EventsDispatcherOwner<T> {
    val eventsDispatcher: EventsDispatcher<T>
}