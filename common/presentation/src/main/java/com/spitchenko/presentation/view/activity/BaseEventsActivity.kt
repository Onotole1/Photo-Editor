package com.spitchenko.presentation.view.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.spitchenko.presentation.viewmodel.EventsDispatcherOwner

abstract class BaseEventsActivity<DB : ViewDataBinding, VM, EL> :
    BaseActivity<DB, VM>() where VM : ViewModel, VM : EventsDispatcherOwner<EL> {

    protected abstract val eventsListener: EL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.eventsDispatcher.bind(this, eventsListener)
    }
}