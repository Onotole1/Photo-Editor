package com.example.photoeditor.utils.databinding

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter

object ImageViewAdapters {

    @JvmStatic
    @BindingAdapter("imageBitmapNotNull")
    fun ImageView.setImageBitmapNotNull(bitmap: Bitmap?) {
        setImageBitmap(bitmap ?: return)
    }

}