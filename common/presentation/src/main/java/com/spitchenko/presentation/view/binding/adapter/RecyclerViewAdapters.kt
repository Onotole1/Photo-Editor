package com.spitchenko.presentation.view.binding.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.spitchenko.presentation.viewmodel.binding.BindingClass

object RecyclerViewAdapters {

    @JvmStatic
    @BindingAdapter("bindingList")
    fun RecyclerView.setBindingList(bindingList: List<BindingClass>?) {
        val bindingAdapter = adapter as? BinderAdapter ?: return

        val newList = bindingList.orEmpty()

        val result = DiffUtilCallback(bindingAdapter.itemList, newList)
            .let {
            DiffUtil.calculateDiff(it)
        }

        bindingAdapter.setItems(result, newList.toList())
    }
}