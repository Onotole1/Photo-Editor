package com.example.photoeditor.feature.main.presentation.viewmodel

import androidx.databinding.ObservableField
import com.example.photoeditor.feature.main.presentation.viewmodel.bindings.ItemControllerBinding
import com.example.photoeditor.shared.presentation.viewmodel.EventsDispatcher
import com.example.photoeditor.utils.databinding.adapter.BindingClass

class MainViewModel(val eventsDispatcher: EventsDispatcher<EventsListener>) {
    val bindingList = ObservableField<List<BindingClass>>().apply {
        set(
            listOf(
                ItemControllerBinding(ITEM_CONTROLLER_ID, {
                    eventsDispatcher.dispatchEvent {
                        showImagePickerDialog()
                    }
                }, {

                }, {

                }, {

                })
            )
        )
    }

    private companion object {
        const val ITEM_CONTROLLER_ID = 435L
    }

    interface EventsListener {
        fun showImagePickerDialog()
    }
}