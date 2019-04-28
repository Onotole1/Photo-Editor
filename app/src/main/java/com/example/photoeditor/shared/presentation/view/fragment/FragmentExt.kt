package com.example.photoeditor.shared.presentation.view.fragment

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment

private const val EXTRA_ARGS = "com.example.photoeditor.shared.presentation.view.fragment.EXTRA_ARGS"

fun Fragment.putArgs(args: Parcelable) = apply {
    arguments = Bundle().apply {
        putParcelable(EXTRA_ARGS, args)
    }
}

fun <ArgsType : Parcelable> Bundle.getArgs(): ArgsType? = getParcelable(EXTRA_ARGS)