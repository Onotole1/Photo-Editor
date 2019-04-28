package com.example.photoeditor.shared.presentation.view.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.photoeditor.BR
import com.example.photoeditor.shared.presentation.viewmodel.EventsDispatcherOwner
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseEventsActivity<DB : ViewDataBinding, VM, EL> :
    BaseActivity<DB, VM>() where VM : ViewModel, VM : EventsDispatcherOwner<EL> {

    protected abstract val eventsListener: EL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.eventsDispatcher.bind(this, eventsListener)
    }
}