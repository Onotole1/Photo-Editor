package com.example.photoeditor.feature.main.presentation.viewmodel.bindings

import android.graphics.Bitmap
import androidx.databinding.ViewDataBinding
import com.example.photoeditor.BR
import com.example.photoeditor.R
import com.example.photoeditor.utils.databinding.adapter.BindingClass

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