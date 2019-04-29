package com.example.photoeditor.feature.main.presentation.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableField
import com.example.photoeditor.feature.main.presentation.viewmodel.bindings.ItemControllerBinding
import com.example.photoeditor.feature.main.presentation.viewmodel.bindings.ItemProgressBinding
import com.example.photoeditor.feature.main.presentation.viewmodel.bindings.ItemResultBinding
import com.example.photoeditor.shared.domain.model.State
import com.example.photoeditor.shared.domain.usecase.DefaultObserver
import com.example.photoeditor.shared.domain.usecase.UseCase
import com.example.photoeditor.shared.presentation.viewmodel.BaseViewModel
import com.example.photoeditor.shared.presentation.viewmodel.EventsDispatcher
import com.example.photoeditor.shared.presentation.viewmodel.EventsDispatcherOwner
import com.example.photoeditor.utils.databinding.adapter.BindingClass
import com.example.photoeditor.utils.databinding.withChangedCallback

class MainViewModel(
    override val eventsDispatcher: EventsDispatcher<EventsListener>,
    private val getBitmapFromUri: UseCase<Bitmap, Uri>,
    private val rotateBitmap: UseCase<State<Bitmap>, Bitmap>,
    private val mirrorBitmap: UseCase<State<Bitmap>, Bitmap>,
    private val invertBitmap: UseCase<State<Bitmap>, Bitmap>
) : BaseViewModel(getBitmapFromUri, rotateBitmap, mirrorBitmap, invertBitmap),
    EventsDispatcherOwner<MainViewModel.EventsListener> {

    private val items = ObservableArrayMap<Long, BindingClass>().withChangedCallback { source, key ->
        updateList(source, key)
    }

    val bindingList = ObservableField<List<BindingClass>>()

    init {
        items[ITEM_CONTROLLER_ID] = ItemControllerBinding(ITEM_CONTROLLER_ID, this)
    }

    fun setImage(uri: Uri) {
        getBitmapFromUri.execute(bitmapSelectObserver(), uri)
    }

    private fun bitmapSelectObserver() = object : DefaultObserver<Bitmap>() {

        override fun onNext(value: Bitmap) {
            val controller = items[ITEM_CONTROLLER_ID] ?: return

            items[ITEM_CONTROLLER_ID] = ItemControllerBinding(controller.itemId, this@MainViewModel, value)
        }

        override fun onError(e: Throwable) {

        }

    }

    fun onSelectImageClick() {
        eventsDispatcher.dispatchEvent {
            showImagePickerDialog()
        }
    }

    fun onRotateClick(bitmap: Bitmap) {
        executeTransform(rotateBitmap, bitmap)
    }

    fun onInvertColorsClick(bitmap: Bitmap) {
        executeTransform(invertBitmap, bitmap)
    }

    fun onMirrorImageClick(bitmap: Bitmap) {
        executeTransform(mirrorBitmap, bitmap)
    }

    private fun executeTransform(transform: UseCase<State<Bitmap>, Bitmap>, bitmap: Bitmap) {
        val newId = bindingList.get()?.lastOrNull()?.itemId?.inc() ?: return

        items[newId] = ItemProgressBinding(newId)
        transform.execute(transformObserver(newId), bitmap)
    }

    private fun updateList(source: Map<Long, BindingClass>, key: Long) {
        val newItem = source[key]

        val newList = bindingList.get().orEmpty().toMutableList()

        if (newItem == null) {
            newList.removeAll {
                it.itemId == key
            }
        } else {
            newList.indexOf(newItem).let {
                newList.apply {
                    if (it >= 0) {
                        this[it] = newItem
                    } else {
                        add(newItem)
                    }
                }
            }
        }

        bindingList.set(newList)
    }

    private fun transformObserver(itemId: Long) = object : DefaultObserver<State<Bitmap>>() {
        override fun onNext(value: State<Bitmap>) {

            when (value) {
                is State.Progress -> items[itemId] = ItemProgressBinding(itemId, value.progress ?: 0)
                is State.Data -> {
                    items[itemId] = ItemResultBinding(
                        itemId,
                        this@MainViewModel,
                        value.data
                    )
                }
            }
        }

        override fun onError(e: Throwable) {
            items.remove(itemId)
        }
    }

    private companion object {
        const val ITEM_CONTROLLER_ID = 435L
    }

    interface EventsListener {
        fun showImagePickerDialog()
    }
}