package com.example.photoeditor.feature.main.presentation.viewmodel.bindings

import androidx.annotation.ColorRes
import androidx.databinding.ViewDataBinding
import com.example.photoeditor.BR
import com.example.photoeditor.R
import com.example.photoeditor.utils.databinding.adapter.BindingClass

class ItemProgressBinding(
    val itemId: Long,
    @ColorRes val backgroundColor: Int,
    val progress: Int = 0
) : BindingClass() {
    override val layoutId: Int = R.layout.item_progress

    override fun areContentsTheSame(other: Any?): Boolean {
        return progress == (other as? ItemProgressBinding)?.progress
    }

    override fun areItemsTheSame(other: Any?): Boolean {
        return itemId == (other as? ItemProgressBinding)?.itemId
    }

    override fun bind(viewDataBinding: ViewDataBinding) {
        viewDataBinding.setVariable(BR.progress, progress)
        viewDataBinding.setVariable(BR.backgroundColor, backgroundColor)
    }
}