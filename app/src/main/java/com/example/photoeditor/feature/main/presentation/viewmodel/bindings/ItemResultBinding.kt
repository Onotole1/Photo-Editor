package com.example.photoeditor.feature.main.presentation.viewmodel.bindings

import android.graphics.Bitmap
import androidx.annotation.ColorRes
import androidx.databinding.ViewDataBinding
import com.example.photoeditor.BR
import com.example.photoeditor.R
import com.example.photoeditor.feature.main.presentation.viewmodel.MainViewModel
import com.example.photoeditor.utils.databinding.adapter.BindingClass

class ItemResultBinding(
    override val itemId: Long,
    private val viewModel: MainViewModel,
    private val image: Bitmap? = null
) : BindingClass() {
    override val layoutId: Int = R.layout.item_result

    override fun areContentsTheSame(other: Any?): Boolean {
        return image?.sameAs((other as? ItemResultBinding)?.image) == true
    }

    override fun areItemsTheSame(other: Any?): Boolean {
        return itemId == (other as? ItemResultBinding)?.itemId
    }

    override fun bind(viewDataBinding: ViewDataBinding) {
        viewDataBinding.apply {
            setVariable(BR.image, image)
            setVariable(BR.viewModel, viewModel)
        }
    }
}