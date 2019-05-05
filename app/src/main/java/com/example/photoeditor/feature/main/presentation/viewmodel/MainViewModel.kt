package com.example.photoeditor.feature.main.presentation.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableField
import com.example.photoeditor.feature.main.domain.entity.BitmapWithId
import com.example.photoeditor.feature.main.presentation.viewmodel.bindings.ItemControllerBinding
import com.example.photoeditor.feature.main.presentation.viewmodel.bindings.ItemProgressBinding
import com.example.photoeditor.feature.main.presentation.viewmodel.bindings.ItemResultBinding
import com.example.photoeditor.shared.domain.model.State
import com.example.photoeditor.shared.domain.usecase.DefaultCompletableObserver
import com.example.photoeditor.shared.domain.usecase.DefaultObserver
import com.example.photoeditor.shared.domain.usecase.UseCase
import com.example.photoeditor.shared.domain.usecase.UseCaseCompletable
import com.example.photoeditor.shared.presentation.viewmodel.BaseViewModel
import com.example.photoeditor.shared.presentation.viewmodel.EventsDispatcher
import com.example.photoeditor.shared.presentation.viewmodel.EventsDispatcherOwner
import com.example.photoeditor.utils.databinding.adapter.BindingClass
import com.example.photoeditor.utils.databinding.withChangedCallback

class MainViewModel(
    override val eventsDispatcher: EventsDispatcher<EventsListener>,
    private val getBitmapFromUri: UseCase<Bitmap, Uri>,
    private val rotateBitmap: UseCase<State<Bitmap>, BitmapWithId>,
    private val mirrorBitmap: UseCase<State<Bitmap>, BitmapWithId>,
    private val invertBitmap: UseCase<State<Bitmap>, BitmapWithId>,
    private val getBitmapFromUrl: UseCase<State<Bitmap>, String>,
    private val removeResult: UseCaseCompletable<Long>,
    getResults: UseCase<List<BitmapWithId>, Unit>
) : BaseViewModel(
    getBitmapFromUri,
    rotateBitmap,
    mirrorBitmap,
    invertBitmap,
    getBitmapFromUrl,
    removeResult,
    getResults
),
    EventsDispatcherOwner<MainViewModel.EventsListener> {

    private val items = ObservableArrayMap<Long, BindingClass>().withChangedCallback { source, key ->
        updateList(source, key)
    }

    private var selectedItem: Long? = null

    val bindingList = ObservableField<List<BindingClass>>()

    init {
        items[ITEM_CONTROLLER_ID] = ItemControllerBinding(ITEM_CONTROLLER_ID, this)

        getResults.execute(resultsObserver(), Unit)
    }

    fun setImage(uri: Uri) {
        getBitmapFromUri.execute(bitmapSelectObserver(), uri)
    }

    private fun bitmapSelectObserver() = object : DefaultObserver<Bitmap>() {

        override fun onNext(value: Bitmap) {
            updateControllerImage(value)
        }

        override fun onError(e: Throwable) {
            eventsDispatcher.dispatchEvent { showError(e) }
        }

    }

    private fun updateControllerImage(bitmap: Bitmap) {
        val controller = items[ITEM_CONTROLLER_ID] ?: return

        items[ITEM_CONTROLLER_ID] = ItemControllerBinding(controller.itemId, this@MainViewModel, bitmap)
    }

    private fun updateControllerProgress(progress: Int?) {
        val controller = items[ITEM_CONTROLLER_ID] as? ItemControllerBinding ?: return

        items[ITEM_CONTROLLER_ID] =
            ItemControllerBinding(controller.itemId, this@MainViewModel, controller.image, progress)
    }

    fun downloadImageByUrl(url: String) {
        getBitmapFromUrl.execute(bitmapDownloadObserver(), url)
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

    fun onImageClick(itemId: Long) {
        selectedItem = itemId
        eventsDispatcher.dispatchEvent { showReplaceOrRemoveDialog() }
    }

    fun removeImage() {
        selectedItem?.also {
            removeResult.execute(removeResultObserver(it), it)
        }
    }

    fun replaceExistingImage() {
        val selectedItem = items.getValue(selectedItem ?: return) as ItemResultBinding

        items[ITEM_CONTROLLER_ID] = ItemControllerBinding(ITEM_CONTROLLER_ID, this, selectedItem.image)
    }

    private fun executeTransform(transform: UseCase<State<Bitmap>, BitmapWithId>, bitmap: Bitmap) {
        val newId = bindingList.get()?.lastOrNull()?.itemId?.inc() ?: return

        items[newId] = ItemProgressBinding(newId)
        transform.execute(
            transformObserver(newId),
            BitmapWithId(newId, bitmap)
        )
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
            eventsDispatcher.dispatchEvent { showError(e) }
        }
    }

    private fun removeResultObserver(itemId: Long) = object : DefaultCompletableObserver() {
        override fun onComplete() {
            items.remove(itemId)
        }

        override fun onError(e: Throwable) {
            eventsDispatcher.dispatchEvent { showError(e) }
        }
    }

    private fun resultsObserver() = object : DefaultObserver<List<BitmapWithId>>() {
        override fun onNext(value: List<BitmapWithId>) {
            value.forEach {
                items[it.imageId] = ItemResultBinding(it.imageId, this@MainViewModel, it.source)
            }
        }
    }

    private fun bitmapDownloadObserver() = object : DefaultObserver<State<Bitmap>>() {
        override fun onNext(value: State<Bitmap>) {
            when (value) {
                is State.Data -> updateControllerImage(value.data)
                is State.Progress -> updateControllerProgress(value.progress)
            }
        }

        override fun onError(e: Throwable) {
            updateControllerProgress(null)
            eventsDispatcher.dispatchEvent { showError(e) }
        }
    }


    private companion object {
        const val ITEM_CONTROLLER_ID = 435L
    }

    interface EventsListener {
        fun showImagePickerDialog()
        fun showReplaceOrRemoveDialog()
        fun showError(throwable: Throwable)
    }
}