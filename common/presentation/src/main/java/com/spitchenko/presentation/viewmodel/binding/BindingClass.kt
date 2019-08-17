package com.spitchenko.presentation.viewmodel.binding

import androidx.databinding.ViewDataBinding

interface BindingClass {

    val layoutId: Int

    val itemId: Long

    fun areContentsTheSame(other: Any?): Boolean

    fun areItemsTheSame(other: Any?): Boolean = (this as? BindingClass)?.itemId == itemId

    fun bind(viewDataBinding: ViewDataBinding)
}