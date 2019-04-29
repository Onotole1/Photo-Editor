package com.example.photoeditor.utils.databinding

import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

object ViewAdapters {

    @JvmStatic
    @BindingAdapter("visibleOrGone")
    fun View.setVisibleOrGone(visibleOrGone: Boolean?) {
        visibility = if (visibleOrGone == true) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}