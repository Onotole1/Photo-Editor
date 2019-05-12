package com.spitchenko.photoeditor.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun Context.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this, permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun Activity.requestPermissions(requestCode: Int, vararg permissions: String) {
    ActivityCompat.requestPermissions(this, permissions, requestCode)
}