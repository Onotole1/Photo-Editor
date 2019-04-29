package com.example.photoeditor.utils.databinding.adapter

import androidx.databinding.ViewDataBinding


abstract class BindingClass {

    abstract val layoutId: Int

    abstract val itemId: Long

    abstract fun areContentsTheSame(other: Any?): Boolean

    abstract fun areItemsTheSame(other: Any?): Boolean

    abstract fun bind(viewDataBinding: ViewDataBinding)

    override fun equals(other: Any?): Boolean {
        return other is BindingClass && other.itemId == itemId
    }

    override fun hashCode(): Int {
        return (itemId * 31).toInt()
    }
}
