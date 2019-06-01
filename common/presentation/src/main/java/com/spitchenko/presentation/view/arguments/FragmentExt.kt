package com.spitchenko.presentation.view.arguments

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment


fun Fragment.putArgs(args: Parcelable) = apply {
    arguments = Bundle().putArgs(args)
}

fun <ArgsType : Parcelable> Fragment.getArgs(): ArgsType? = arguments?.getArgs()