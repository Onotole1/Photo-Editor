package com.spitchenko.presentation.view.arguments

import android.os.Bundle
import android.os.Parcelable

private const val EXTRA_ARGS = "com.spitchenko.presentation.view.arguments.EXTRA_ARGS"

fun Bundle.putArgs(args: Parcelable) = apply {
    putParcelable(EXTRA_ARGS, args)
}

fun <ArgsType : Parcelable> Bundle.getArgs(): ArgsType? = getParcelable(EXTRA_ARGS)