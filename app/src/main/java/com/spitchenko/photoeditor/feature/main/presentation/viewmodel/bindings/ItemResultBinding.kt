package com.spitchenko.photoeditor.feature.main.presentation.viewmodel.bindings

import android.graphics.Bitmap
import androidx.databinding.ViewDataBinding
import com.spitchenko.photoeditor.BR
import com.spitchenko.photoeditor.R
import com.spitchenko.presentation.viewmodel.binding.BindingClass

class ItemResultBinding(
    override val itemId: Long,
    val image: Bitmap? = null
) : BindingClass() {
    override val layoutId: Int = R.layout.item_result

    override fun areContentsTheSame(other: Any?): Boolean {
        return image?.sameAs((other as? ItemResultBinding)?.image) == true
    }

    override fun bind(viewDataBinding: ViewDataBinding) {
        viewDataBinding.apply {
            setVariable(BR.image, image)
        }
    }
}