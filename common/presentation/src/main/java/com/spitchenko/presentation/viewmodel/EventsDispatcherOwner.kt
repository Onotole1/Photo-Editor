package com.spitchenko.presentation.viewmodel

interface EventsDispatcherOwner<T> {
    val eventsDispatcher: EventsDispatcher<T>
}