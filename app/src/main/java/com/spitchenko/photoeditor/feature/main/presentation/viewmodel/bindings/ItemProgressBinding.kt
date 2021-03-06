package com.spitchenko.photoeditor.feature.main.presentation.viewmodel.bindings

import androidx.databinding.ViewDataBinding
import com.spitchenko.photoeditor.BR
import com.spitchenko.photoeditor.R
import com.spitchenko.presentation.viewmodel.binding.BindingClass

class ItemProgressBinding(
    override val itemId: Long,
    val progress: Int = 0
) : BindingClass {
    override val layoutId: Int = R.layout.item_progress

    override fun areContentsTheSame(other: Any?): Boolean {
        return progress == (other as? ItemProgressBinding)?.progress
    }

    override fun bind(viewDataBinding: ViewDataBinding) {
        viewDataBinding.setVariable(BR.progress, progress)
    }
}