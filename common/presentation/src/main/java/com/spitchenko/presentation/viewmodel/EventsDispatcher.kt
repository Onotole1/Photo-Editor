package com.spitchenko.presentation.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import java.util.*

/**
 * Класс предоставляет очередь событий для их обработки в момент жизненного цикла от onResume() до onPause()
 *
 * Рекомендуется подключить зависимость kapt "androidx.lifecycle:lifecycle-compiler:$lifecycle_version",
 * иначе будет использоваться рефлексия для нотификации о событиях жизненного цикла.
 *
 * @param ListenerType - тип слушателя, в контексте которого происходят события
 */
class EventsDispatcher<ListenerType> : LifecycleObserver {

    internal var eventsListener: ListenerType? = null

    internal var boundListener: ListenerType? = null

    internal val blocks = LinkedList<ListenerType.() -> Unit>()

    fun bind(lifecycleOwner: LifecycleOwner, listener: ListenerType) {
        eventsListener = listener

        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun connectListener() {
        boundListener = eventsListener

        val listener = boundListener ?: return

        blocks.forEach {
            it(listener)
        }

        blocks.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun disconnectListener() {
        boundListener = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun clear(source: LifecycleOwner) {
        eventsListener = null
        boundListener = null
        source.lifecycle.removeObserver(this)
    }

    fun dispatchEvent(block: ListenerType.() -> Unit) {
        boundListener?.also(block) ?: blocks.add(block)
    }
}