package com.spitchenko.photoeditor.feature.main.presentation.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.databinding.ObservableList
import com.spitchenko.domain.model.State
import com.spitchenko.photoeditor.feature.main.domain.entity.BitmapWithId
import com.spitchenko.photoeditor.feature.main.domain.entity.SetImageRequest
import com.spitchenko.photoeditor.feature.main.domain.entity.UriWithId
import com.spitchenko.photoeditor.feature.main.presentation.viewmodel.bindings.ItemControllerBinding
import com.spitchenko.photoeditor.feature.main.presentation.viewmodel.bindings.ItemProgressBinding
import com.spitchenko.photoeditor.feature.main.presentation.viewmodel.bindings.ItemResultBinding
import com.spitchenko.photoeditor.utils.addTo
import com.spitchenko.presentation.viewmodel.BaseViewModel
import com.spitchenko.presentation.viewmodel.EventsDispatcher
import com.spitchenko.presentation.viewmodel.binding.BindingClass
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class MainViewModel(
    private val useCases: MainViewModelUseCases,
    val eventsDispatcher: EventsDispatcher<EventsListener>,
    val bindingList: ObservableList<BindingClass>
) : BaseViewModel() {

    private var selectedItemPosition: Int? = null

    fun init() {
        bindingList.add(ItemControllerBinding(ITEM_CONTROLLER_ID, this))

        useCases.getResults()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(resultsObserver())
    }

    fun setImage(uri: Uri) = useCases.getBitmapFromUri(UriWithId(uri, ITEM_CONTROLLER_ID))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(bitmapDownloadObserver())

    private fun updateControllerImage(bitmap: Bitmap) {
        val controller = bindingList[0]

        bindingList[0] = ItemControllerBinding(controller.itemId, this@MainViewModel, bitmap)
    }

    private fun updateControllerProgress(progress: Int?) {
        val controller = bindingList[0] as? ItemControllerBinding ?: return

        bindingList[0] =
            ItemControllerBinding(controller.itemId, this@MainViewModel, controller.image, progress)
    }

    fun onExifClick() = useCases.getExif()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(exifObserver())

    fun downloadImageByUrl(url: String) =
        useCases.getBitmapFromUri(UriWithId(Uri.parse(url), ITEM_CONTROLLER_ID))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(bitmapDownloadObserver())

    fun onSelectImageClick() = eventsDispatcher.dispatchEvent {
        showImagePickerDialog()
    }

    fun onRotateClick(bitmap: Bitmap) = executeTransform(bitmap) {
        useCases.rotateBitmap(it)
    }

    fun onInvertColorsClick(bitmap: Bitmap) = executeTransform(bitmap) {
        useCases.invertBitmap(it)
    }

    fun onMirrorImageClick(bitmap: Bitmap) = executeTransform(bitmap) {
        useCases.mirrorBitmap(it)
    }

    fun onImageClick(itemPosition: Int) {
        selectedItemPosition = itemPosition
        eventsDispatcher.dispatchEvent { showReplaceOrRemoveDialog() }
    }

    fun removeImage() = selectedItemPosition?.also {
        val itemId = bindingList[it].itemId

        useCases.removeResult(itemId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(removeResultObserver(itemId))
    }

    fun replaceExistingImage() {
        val selectedItem = bindingList.getOrNull(selectedItemPosition ?: return) as ItemResultBinding

        val bitmap = selectedItem.image ?: return

        useCases.setControllerImage(SetImageRequest(selectedItem.itemId, ITEM_CONTROLLER_ID))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(setControllerImageObserver(bitmap))
    }

    private fun exifObserver() = object : SingleObserver<Map<String, String>> {

        override fun onSubscribe(disposable: Disposable) = disposable.addTo(disposables)

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

    private inline fun executeTransform(
        bitmap: Bitmap,
        transform: (bitmapWIthId: BitmapWithId) -> Observable<State<Bitmap>>
    ) {
        val newId = bindingList.lastOrNull()?.itemId?.inc() ?: return

        bindingList.add(ItemProgressBinding(newId))

        transform(BitmapWithId(newId, bitmap))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(transformObserver(bindingList.lastIndex, newId))
    }

    private fun setControllerImageObserver(bitmap: Bitmap) = object : CompletableObserver {

        override fun onSubscribe(disposable: Disposable) = disposable.addTo(disposables)

        override fun onComplete() {
            bindingList[0] = ItemControllerBinding(ITEM_CONTROLLER_ID, this@MainViewModel, bitmap)
        }

        override fun onError(e: Throwable) {
            eventsDispatcher.dispatchEvent { showError(e) }
        }
    }

    private fun transformObserver(itemPosition: Int, itemId: Long) =
        object : Observer<State<Bitmap>> {

        var currentPosition = itemPosition

            override fun onComplete() = Unit

            override fun onSubscribe(disposable: Disposable) = disposable.addTo(disposables)

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

    private fun removeResultObserver(itemId: Long) = object : CompletableObserver {

        override fun onSubscribe(disposable: Disposable) = disposable.addTo(disposables)

        override fun onComplete() {
            bindingList.removeAll {
                it.itemId == itemId
            }
        }

        override fun onError(e: Throwable) {
            eventsDispatcher.dispatchEvent { showError(e) }
        }
    }

    private fun resultsObserver() = object : SingleObserver<List<BitmapWithId>> {

        override fun onSubscribe(disposable: Disposable) = disposable.addTo(disposables)

        override fun onError(e: Throwable) = Unit

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

    private fun bitmapDownloadObserver() = object : Observer<State<Bitmap>> {

        override fun onComplete() = Unit

        override fun onSubscribe(disposable: Disposable) = disposable.addTo(disposables)

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