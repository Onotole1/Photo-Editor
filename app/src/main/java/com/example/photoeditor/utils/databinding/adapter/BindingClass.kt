package com.example.photoeditor.utils.databinding.adapter

import androidx.databinding.ViewDataBinding


abstract class BindingClass {

    abstract val layoutId: Int

    abstract fun areContentsTheSame(other: Any?): Boolean

    abstract fun areItemsTheSame(other: Any?): Boolean

    abstract fun bind(viewDataBinding: ViewDataBinding)
}
