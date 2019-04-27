package com.example.photoeditor.utils.databinding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.photoeditor.utils.databinding.adapter.BinderAdapter
import com.example.photoeditor.utils.databinding.adapter.BindingClass
import com.example.photoeditor.utils.databinding.adapter.DiffUtilCallback

object RecyclerViewAdapters {

    @JvmStatic
    @BindingAdapter("bindingList")
    fun RecyclerView.setVisibleOrGone(bindingList: List<BindingClass>?) {
        val bindingAdapter = adapter as? BinderAdapter ?: return

        val newList = bindingList.orEmpty()

        val result = DiffUtilCallback(bindingAdapter.itemList, newList).let {
            DiffUtil.calculateDiff(it)
        }

        bindingAdapter.setItems(result, newList)
    }
}