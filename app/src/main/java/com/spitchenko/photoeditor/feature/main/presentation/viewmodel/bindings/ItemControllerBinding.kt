package com.spitchenko.photoeditor.feature.main.presentation.viewmodel.bindings

import android.graphics.Bitmap
import androidx.databinding.ViewDataBinding
import com.spitchenko.photoeditor.BR
import com.spitchenko.photoeditor.R
import com.spitchenko.photoeditor.feature.main.presentation.viewmodel.MainViewModel
import com.spitchenko.presentation.viewmodel.binding.BindingClass

class ItemControllerBinding(
    override val itemId: Long,
    private val viewModel: MainViewModel,
    val image: Bitmap? = null,
    val progress: Int? = null
) : BindingClass {
    override val layoutId: Int = R.layout.item_controller

    override fun areContentsTheSame(other: Any?): Boolean {
        val otherController = other as? ItemControllerBinding ?: return false

        return image?.sameAs(otherController.image) == true && progress == otherController.progress
    }

    override fun bind(viewDataBinding: ViewDataBinding) {
        viewDataBinding.apply {
            setVariable(BR.image, image)
            setVariable(BR.viewModel, viewModel)
            setVariable(BR.progress, progress)
            setVariable(BR.imageReady, image != null && progress == null)
        }
    }
}