package com.spitchenko.presentation.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import java.util.*

class EventsDispatcher<ListenerType> {
    private var eventsListener: ListenerType? = null

    private val blocks = LinkedList<ListenerType.() -> Unit>()

    fun bind(lifecycleOwner: LifecycleOwner, listener: ListenerType) {
        val observer = object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun connectListener() {
                eventsListener = listener
                    blocks.forEach {
                        it(listener)
                    }
                    blocks.clear()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun disconnectListener() {
                eventsListener = null
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroyed(source: LifecycleOwner) {
                source.lifecycle.removeObserver(this)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
    }

    fun dispatchEvent(block: ListenerType.() -> Unit) {
        val eListener = eventsListener
        if (eListener != null) {
            block(eListener)
        } else {
            blocks.add(block)
        }
    }
}