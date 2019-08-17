package com.spitchenko.presentation.view.binding.adapter

import androidx.recyclerview.widget.DiffUtil
import com.spitchenko.presentation.viewmodel.binding.BindingClass

class DiffUtilCallback(
    private val mOldItems: List<BindingClass>,
    private val mNewItems: List<BindingClass>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        mOldItems[oldItemPosition].areItemsTheSame(mNewItems[newItemPosition])

    override fun getOldListSize(): Int = mOldItems.size

    override fun getNewListSize(): Int = mNewItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        mOldItems[oldItemPosition].areContentsTheSame(mNewItems[newItemPosition])
}