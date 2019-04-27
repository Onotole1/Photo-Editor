package com.example.photoeditor.feature.main.presentation.viewmodel.bindings

import android.graphics.Bitmap
import androidx.databinding.ViewDataBinding
import com.example.photoeditor.BR
import com.example.photoeditor.R
import com.example.photoeditor.utils.databinding.adapter.BindingClass

class ItemControllerBinding(
    private val itemId: Long,
    private val onSelectImageClick: () -> Unit,
    private val onRotateClick: () -> Unit,
    private val onInvertColorsClick: () -> Unit,
    private val onMirrorImageClick: () -> Unit,
    var image: Bitmap? = null
) : BindingClass() {
    override val layoutId: Int = R.layout.item_controller

    override fun areContentsTheSame(other: Any?): Boolean {
        return image == (other as? ItemControllerBinding)?.image
    }

    override fun areItemsTheSame(other: Any?): Boolean {
        return itemId == (other as? ItemControllerBinding)?.itemId
    }

    override fun bind(viewDataBinding: ViewDataBinding) {
        viewDataBinding.apply {
            setVariable(BR.image, image)
            setVariable(BR.onSelectImageClick, onSelectImageClick)
            setVariable(BR.onRotateClick, onRotateClick)
            setVariable(BR.onInvertColorsClick, onInvertColorsClick)
            setVariable(BR.onMirrorImageClick, onMirrorImageClick)
        }
    }
}