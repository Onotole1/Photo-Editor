package com.spitchenko.photoeditor.utils.databinding

import android.view.View
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