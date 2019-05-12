package com.spitchenko.photoeditor.utils

import android.content.Context
import com.spitchenko.photoeditor.R
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

fun Throwable.toUserFriendlyError(context: Context): String {
    return when (this) {
        is ConnectException,
        is SocketTimeoutException,
        is TimeoutException,
        is UnknownHostException -> context.getString(R.string.connection_error)
        is IOException -> context.getString(R.string.no_disk_space_error)
        else -> localizedMessage
    }
}