package com.spitchenko.presentation.view.binding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BindingViewHolder<T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root) {

    constructor(parent: ViewGroup, @LayoutRes mLayout: Int):
            this(DataBindingUtil.inflate<T>(LayoutInflater.from(parent.context), mLayout, parent, false))
}
