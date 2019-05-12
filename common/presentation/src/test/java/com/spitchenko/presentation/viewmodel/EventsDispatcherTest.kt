package com.spitchenko.presentation.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito

class EventsDispatcherTest {

    private lateinit var eventsDispatcher: EventsDispatcher<Listener>

    @Before
    fun setUp() {
        eventsDispatcher = EventsDispatcher()
    }

    @Test
    fun `Initially blocks is empty`() {
        assertTrue(eventsDispatcher.blocks.isEmpty())
    }

    @Test
    fun `Dispatch events without listener`() {
        assertTrue(eventsDispatcher.blocks.isEmpty())
        eventsDispatcher.dispatchEvent { invoke() }
        assertSame(1, eventsDispatcher.blocks.size)
        eventsDispatcher.dispatchEvent { invoke() }
        assertSame(2, eventsDispatcher.blocks.size)
    }

    @Test
    fun `Dispatch events with listener`() {

        eventsDispatcher.dispatchEvent { invoke() }
        eventsDispatcher.dispatchEvent { invoke() }

        val mockListener = Mockito.mock(TestListener::class.java)
        val lifecycleOwner = TestLifecycleOwner()

        lifecycleOwner.callLifecycleEvent(Lifecycle.Event.ON_CREATE)

        eventsDispatcher.bind(lifecycleOwner, mockListener)

        lifecycleOwner.callLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleOwner.callLifecycleEvent(Lifecycle.Event.ON_RESUME)

        Mockito.verify(mockListener, Mockito.times(2)).invoke()
        assertSame(0, eventsDispatcher.blocks.size)

        lifecycleOwner.callLifecycleEvent(Lifecycle.Event.ON_PAUSE)

        eventsDispatcher.dispatchEvent { invoke() }

        Mockito.verify(mockListener, Mockito.times(2)).invoke()

        lifecycleOwner.callLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycleOwner.callLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    @Test
    fun `Dispatch events with listener after onPause and then onResume`() {
        val mockListener = Mockito.mock(TestListener::class.java)
        val lifecycleOwner = TestLifecycleOwner()

        eventsDispatcher.bind(lifecycleOwner, mockListener)

        lifecycleOwner.callLifecycleEvent(Lifecycle.Event.ON_RESUME)
        lifecycleOwner.callLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        lifecycleOwner.callLifecycleEvent(Lifecycle.Event.ON_RESUME)

        eventsDispatcher.dispatchEvent { invoke() }
        Mockito.verify(mockListener, Mockito.times(1)).invoke()
    }

    @Test
    fun `After onDestroy listener is null`() {
        val lifecycleOwner = TestLifecycleOwner()

        val mockListener = Mockito.mock(TestListener::class.java)

        assertNull(eventsDispatcher.eventsListener)

        eventsDispatcher.bind(lifecycleOwner, mockListener)

        assertNotNull(eventsDispatcher.eventsListener)

        assertNull(eventsDispatcher.boundListener)

        lifecycleOwner.callLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleOwner.callLifecycleEvent(Lifecycle.Event.ON_RESUME)

        assertNotNull(eventsDispatcher.boundListener)

        lifecycleOwner.callLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        lifecycleOwner.callLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycleOwner.callLifecycleEvent(Lifecycle.Event.ON_DESTROY)

        assertNull(eventsDispatcher.boundListener)
        assertNull(eventsDispatcher.eventsListener)
    }

    private class TestLifecycleOwner : LifecycleOwner {
        private val lifecycle = TestLifecycle()

        override fun getLifecycle(): Lifecycle {
            return lifecycle
        }

        fun callLifecycleEvent(event: Lifecycle.Event) {
            lifecycle.callMethodAnnotatedEvent(this, event)
        }

        private class TestLifecycle : Lifecycle() {
            private var observer: LifecycleObserver? = null

            override fun addObserver(observer: LifecycleObserver) {
                this.observer = observer
            }

            override fun removeObserver(observer: LifecycleObserver) {
                this.observer = null
            }

            override fun getCurrentState(): State {
                return State.INITIALIZED
            }

            fun callMethodAnnotatedEvent(lifecycleOwner: LifecycleOwner, event: Event) {
                val observer = observer ?: return

                val methods = observer.javaClass.declaredMethods

                val method = methods.firstOrNull {
                    if (it.isAnnotationPresent(OnLifecycleEvent::class.java)) {
                        return@firstOrNull it.getAnnotation(OnLifecycleEvent::class.java).value == event
                    } else {
                        return@firstOrNull false
                    }
                }

                if (method?.parameterCount == 0) {
                    method.invoke(observer)
                } else {
                    method?.invoke(observer, lifecycleOwner)
                }
            }
        }
    }

    open class TestListener : Listener {
        override fun invoke() {
            // do nothing
        }
    }

    interface Listener {
        fun invoke()
    }
}