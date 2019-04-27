package com.example.photoeditor.feature.main.presentation.viewmodel.bindings

import android.graphics.Bitmap
import androidx.databinding.ViewDataBinding
import com.example.photoeditor.BR
import com.example.photoeditor.R
import com.example.photoeditor.utils.databinding.adapter.BindingClass

class ItemResultBinding(
    private val itemId: Long,
    private val onImageClick: () -> Unit,
    var image: Bitmap? = null
) : BindingClass() {
    override val layoutId: Int = R.layout.item_controller

    override fun areContentsTheSame(other: Any?): Boolean {
        return image == (other as? ItemResultBinding)?.image
    }

    override fun areItemsTheSame(other: Any?): Boolean {
        return itemId == (other as? ItemResultBinding)?.itemId
    }

    override fun bind(viewDataBinding: ViewDataBinding) {
        viewDataBinding.apply {
            setVariable(BR.image, image)
            setVariable(BR.onImageClick, onImageClick)
        }
    }
}