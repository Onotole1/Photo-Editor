package com.spitchenko.presentation.view.binding.adapter


import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.spitchenko.presentation.viewmodel.binding.BindingClass

open class BinderAdapter(private val mLifecycleOwner: LifecycleOwner? = null) :
    RecyclerView.Adapter<BindingViewHolder<ViewDataBinding>>() {
    var itemList: List<BindingClass> = emptyList()
        private set

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int =
        itemList.getOrNull(position)?.layoutId ?: super.getItemViewType(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ViewDataBinding> {
        val viewHolder = BindingViewHolder<ViewDataBinding>(parent, viewType)

        viewHolder.binding.lifecycleOwner = mLifecycleOwner

        return viewHolder
    }

    fun setItems(diffResult: DiffUtil.DiffResult, items: List<BindingClass>) {
        itemList = items
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ViewDataBinding>, position: Int) {
        val model = itemList.getOrNull(position) ?: return
        model.bind(holder.binding)
        holder.binding.executePendingBindings()
    }
}
