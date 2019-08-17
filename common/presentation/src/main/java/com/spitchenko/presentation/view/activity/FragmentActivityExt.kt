package com.spitchenko.presentation.view.activity

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.spitchenko.presentation.viewmodel.ViewModelFactory

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(noinline creator: (() -> T)): T =
    ViewModelProviders.of(this, ViewModelFactory(creator)).get(T::class.java)