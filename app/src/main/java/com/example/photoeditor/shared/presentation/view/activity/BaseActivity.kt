package com.example.photoeditor.shared.presentation.view.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity<DB: ViewDataBinding, VM: ViewModel>: DaggerAppCompatActivity() {

    protected abstract val binding: DB

    protected abstract val viewModel: VM

    protected abstract val viewModelVariableId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.setVariable(viewModelVariableId, viewModel)
    }
}