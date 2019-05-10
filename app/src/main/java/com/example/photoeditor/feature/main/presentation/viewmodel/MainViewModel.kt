package com.example.photoeditor.feature.main.presentation.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.databinding.ObservableArrayList
import com.example.photoeditor.feature.main.domain.entity.BitmapWithId
import com.example.photoeditor.feature.main.domain.entity.SetImageRequest
import com.example.photoeditor.feature.main.domain.entity.UriWithId
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

class MainViewModel(
    override val eventsDispatcher: EventsDispatcher<EventsListener>,
    private val getBitmapFromUri: UseCase<State<Bitmap>, UriWithId>,
    private val rotateBitmap: UseCase<State<Bitmap>, BitmapWithId>,
    private val mirrorBitmap: UseCase<State<Bitmap>, BitmapWithId>,
    private val invertBitmap: UseCase<State<Bitmap>, BitmapWithId>,
    private val removeResult: UseCaseCompletable<Long>,
    private val setControllerImage: UseCaseCompletable<SetImageRequest>,
    private val getExif: UseCase<Map<String, String>, Unit>,
    getResults: UseCase<List<BitmapWithId>, Unit>
) : BaseViewModel(
    getBitmapFromUri,
    rotateBitmap,
    mirrorBitmap,
    invertBitmap,
    removeResult,
    setControllerImage,
    getResults
),
    EventsDispatcherOwner<MainViewModel.EventsListener> {

    private var selectedItemPosition: Int? = null

    val bindingList = ObservableArrayList<BindingClass>()

    init {
        bindingList.add(ItemControllerBinding(ITEM_CONTROLLER_ID, this))

        getResults.execute(resultsObserver(), Unit)
    }

    fun setImage(uri: Uri) {
        getBitmapFromUri.execute(bitmapDownloadObserver(), UriWithId(uri, ITEM_CONTROLLER_ID))
    }

    private fun updateControllerImage(bitmap: Bitmap) {
        val controller = bindingList[0] ?: return

        bindingList[0] = ItemControllerBinding(controller.itemId, this@MainViewModel, bitmap)
    }

    private fun updateControllerProgress(progress: Int?) {
        val controller = bindingList[0] as? ItemControllerBinding ?: return

        bindingList[0] =
            ItemControllerBinding(controller.itemId, this@MainViewModel, controller.image, progress)
    }

    fun onExifClick() {
        getExif.execute(exifObserver(), Unit)
    }

    fun downloadImageByUrl(url: String) {
        getBitmapFromUri.execute(bitmapDownloadObserver(), UriWithId(Uri.parse(url), ITEM_CONTROLLER_ID))
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

    fun onImageClick(itemPosition: Int) {
        selectedItemPosition = itemPosition
        eventsDispatcher.dispatchEvent { showReplaceOrRemoveDialog() }
    }

    fun removeImage() {
        selectedItemPosition?.also {
            val itemId = bindingList[it].itemId
            removeResult.execute(removeResultObserver(itemId), itemId)
        }
    }

    fun replaceExistingImage() {
        val selectedItem = bindingList.getOrNull(selectedItemPosition ?: return) as ItemResultBinding

        val bitmap = selectedItem.image ?: return

        setControllerImage.execute(
            setControllerImageObserver(bitmap),
            SetImageRequest(selectedItem.itemId, ITEM_CONTROLLER_ID)
        )
    }

    private fun exifObserver() = object : DefaultObserver<Map<String, String>>() {
        override fun onNext(value: Map<String, String>) {
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

    private fun resultsObserver() = object : DefaultObserver<List<BitmapWithId>>() {
        override fun onNext(value: List<BitmapWithId>) {
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