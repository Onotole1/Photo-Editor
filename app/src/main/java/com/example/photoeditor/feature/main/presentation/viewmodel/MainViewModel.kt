package com.example.photoeditor.feature.main.presentation.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.photoeditor.feature.main.presentation.viewmodel.bindings.ItemControllerBinding
import com.example.photoeditor.shared.domain.usecase.DefaultObserver
import com.example.photoeditor.shared.domain.usecase.UseCase
import com.example.photoeditor.shared.presentation.viewmodel.EventsDispatcher
import com.example.photoeditor.shared.presentation.viewmodel.EventsDispatcherOwner
import com.example.photoeditor.utils.databinding.adapter.BindingClass

class MainViewModel(
    override val eventsDispatcher: EventsDispatcher<EventsListener>,
    private val getBitmapFromUri: UseCase<Bitmap, Uri>
) : ViewModel(),
    EventsDispatcherOwner<MainViewModel.EventsListener> {

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

    fun setImage(uri: Uri) {
        getBitmapFromUri.execute(bitmapObserver(), uri)
    }

    private fun bitmapObserver() = object : DefaultObserver<Bitmap>() {
        override fun onNext(value: Bitmap) {
            val currentList = bindingList.get()

            val controller = currentList?.getOrNull(0) as? ItemControllerBinding ?: return

            bindingList.set(currentList.toMutableList().apply {
                set(0, controller.copy(image = value))
            })
        }

        override fun onError(e: Throwable) {

        }

    }

    private companion object {
        const val ITEM_CONTROLLER_ID = 435L
    }

    interface EventsListener {
        fun showImagePickerDialog()
    }
}