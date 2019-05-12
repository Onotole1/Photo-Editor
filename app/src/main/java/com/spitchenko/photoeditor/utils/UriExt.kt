package com.spitchenko.photoeditor.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

fun Uri.grantUriPermissionsForAllActivities(context: Context, intent: Intent) {
    val resInfoList = context.packageManager.queryIntentActivities(
        intent, PackageManager.MATCH_DEFAULT_ONLY
    )

    for (resolveInfo in resInfoList) {
        val packageName = resolveInfo.activityInfo.packageName

        context.grantUriPermission(
            packageName,
            this,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    }
}