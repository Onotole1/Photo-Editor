package com.example.photoeditor.feature.main.presentation.viewmodel

import androidx.databinding.ObservableField
import com.example.photoeditor.R
import com.example.photoeditor.feature.main.presentation.viewmodel.bindings.ItemControllerBinding
import com.example.photoeditor.feature.main.presentation.viewmodel.bindings.ItemProgressBinding
import com.example.photoeditor.feature.main.presentation.viewmodel.bindings.ItemResultBinding
import com.example.photoeditor.shared.presentation.viewmodel.BaseViewModel
import com.example.photoeditor.utils.databinding.adapter.BindingClass

class MainViewModel: BaseViewModel() {
    val bindingList = ObservableField<List<BindingClass>>()
}