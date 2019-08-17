package com.spitchenko.photoeditor.feature.main.presentation.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.databinding.ObservableList
import com.spitchenko.domain.model.State
import com.spitchenko.domain.usecase.DefaultCompletableObserver
import com.spitchenko.domain.usecase.DefaultObserver
import com.spitchenko.domain.usecase.DefaultSingleObserver
import com.spitchenko.domain.usecase.UseCase
import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId
import com.spitchenko.photoeditor.feature.main.domain.entity.SetImageRequest
import com.spitchenko.photoeditor.feature.main.domain.entity.UriWithId
import com.spitchenko.photoeditor.feature.main.presentation.viewmodel.bindings.ItemControllerBinding
import com.spitchenko.photoeditor.feature.main.presentation.viewmodel.bindings.ItemProgressBinding
import com.spitchenko.photoeditor.feature.main.presentation.viewmodel.bindings.ItemResultBinding
import com.spitchenko.presentation.viewmodel.BaseViewModel
import com.spitchenko.presentation.viewmodel.EventsDispatcher
import com.spitchenko.presentation.viewmodel.binding.BindingClass

class MainViewModel(
    private val useCases: MainViewModelUseCases,
    val eventsDispatcher: EventsDispatcher<EventsListener>,
    val bindingList: ObservableList<BindingClass>
) : BaseViewModel(
    useCases
) {

    private var selectedItemPosition: Int? = null

    init {
        bindingList.add(ItemControllerBinding(ITEM_CONTROLLER_ID, this))

        useCases.getResults.execute(resultsObserver(), Unit)
    }

    fun setImage(uri: Uri) =
        useCases.getBitmapFromUri.execute(bitmapDownloadObserver(), UriWithId(uri, ITEM_CONTROLLER_ID))

    private fun updateControllerImage(bitmap: Bitmap) {
        val controller = bindingList[0]

        bindingList[0] = ItemControllerBinding(controller.itemId, this@MainViewModel, bitmap)
    }

    private fun updateControllerProgress(progress: Int?) {
        val controller = bindingList[0] as? ItemControllerBinding ?: return

        bindingList[0] =
            ItemControllerBinding(controller.itemId, this@MainViewModel, controller.image, progress)
    }

    fun onExifClick() = useCases.getExif.execute(exifObserver(), Unit)

    fun downloadImageByUrl(url: String) =
        useCases.getBitmapFromUri.execute(bitmapDownloadObserver(), UriWithId(Uri.parse(url), ITEM_CONTROLLER_ID))

    fun onSelectImageClick() = eventsDispatcher.dispatchEvent {
        showImagePickerDialog()
    }

    fun onRotateClick(bitmap: Bitmap) = executeTransform(useCases.rotateBitmap, bitmap)

    fun onInvertColorsClick(bitmap: Bitmap) = executeTransform(useCases.invertBitmap, bitmap)

    fun onMirrorImageClick(bitmap: Bitmap) = executeTransform(useCases.mirrorBitmap, bitmap)

    fun onImageClick(itemPosition: Int) {
        selectedItemPosition = itemPosition
        eventsDispatcher.dispatchEvent { showReplaceOrRemoveDialog() }
    }

    fun removeImage() =
        selectedItemPosition?.also {
            val itemId = bindingList[it].itemId
            useCases.removeResult.execute(removeResultObserver(itemId), itemId)
        }

    fun replaceExistingImage() {
        val selectedItem = bindingList.getOrNull(selectedItemPosition ?: return) as ItemResultBinding

        val bitmap = selectedItem.image ?: return

        useCases.setControllerImage.execute(
            setControllerImageObserver(bitmap),
            SetImageRequest(selectedItem.itemId, ITEM_CONTROLLER_ID)
        )
    }

    private fun exifObserver() = object : DefaultSingleObserver<Map<String, String>>() {
        override fun onSuccess(value: Map<String, String>) {
            val exifInfo = value.entries.joinToString("\n") {
                "${it.key}:${it.value}"
            }

            eventsDispatcher.dispatchEvent { showExifInfo(exifInfo) }
        }

        override fun onError(e: Throwable) {
            eventsDispatcher.dispatchEvent { showError(e) }
        }
    }

    private fun executeTransform(transform: UseCase<State<Bitmap>, BitmapWithId>, bitmap: Bitmap) {
        val newId = bindingList.lastOrNull()?.itemId?.inc() ?: return

        bindingList.add(ItemProgressBinding(newId))

        transform.execute(
            transformObserver(bindingList.lastIndex, newId),
            BitmapWithId(newId, bitmap)
        )
    }

    private fun setControllerImageObserver(bitmap: Bitmap) = object : DefaultCompletableObserver() {
        override fun onComplete() {
            bindingList[0] = ItemControllerBinding(ITEM_CONTROLLER_ID, this@MainViewModel, bitmap)
        }

        override fun onError(e: Throwable) {
            eventsDispatcher.dispatchEvent { showError(e) }
        }
    }

    private fun transformObserver(itemPosition: Int, itemId: Long) = object : DefaultObserver<State<Bitmap>>() {
        var currentPosition = itemPosition

        override fun onNext(value: State<Bitmap>) {

            if (bindingList.getOrNull(currentPosition)?.itemId != itemId) {
                currentPosition = bindingList.indexOfFirst {
                    it.itemId == itemId
                }
            }

            when (value) {
                is State.Progress -> bindingList[currentPosition] = ItemProgressBinding(itemId, value.progress ?: 0)
                is State.Data -> bindingList[currentPosition] = ItemResultBinding(itemId, value.data)
            }
        }

        override fun onError(e: Throwable) {
            bindingList.removeAt(itemPosition)
            eventsDispatcher.dispatchEvent { showError(e) }
        }
    }

    private fun removeResultObserver(itemId: Long) = object : DefaultCompletableObserver() {
        override fun onComplete() {
            bindingList.removeAll {
                it.itemId == itemId
            }
        }

        override fun onError(e: Throwable) {
            eventsDispatcher.dispatchEvent { showError(e) }
        }
    }

    private fun resultsObserver() = object : DefaultSingleObserver<List<BitmapWithId>>() {
        override fun onSuccess(value: List<BitmapWithId>) {
            value.sortedBy {
                it.imageId
            }.forEach {
                if (it.imageId == ITEM_CONTROLLER_ID) {
                    bindingList[0] = ItemControllerBinding(it.imageId, this@MainViewModel, it.source)
                } else {
                    bindingList.add(ItemResultBinding(it.imageId, it.source))
                }
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
        const val ITEM_CONTROLLER_ID = 0L
    }

    interface EventsListener {
        fun showImagePickerDialog()
        fun showReplaceOrRemoveDialog()
        fun showError(throwable: Throwable)
        fun showExifInfo(exif: String)
    }
}