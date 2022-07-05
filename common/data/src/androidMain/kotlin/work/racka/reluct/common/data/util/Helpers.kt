package work.racka.reluct.common.data.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

// Used to filter out apps. If an app has a main activity then it is probably not an that we
// need when taking screen time usage.
internal fun hasMainActivity(context: Context, packageName: String): Boolean {
    val packageManager = context.packageManager
    return packageManager.getLaunchIntentForPackage(packageName) != null
}

internal fun hasSystemFlag(
    context: Context,
    appInfo: ApplicationInfo? = null,
    packageName: String
): Boolean = if (appInfo != null) {
    appInfo.flags == ApplicationInfo.FLAG_SYSTEM
} else {
    context.packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        .flags == ApplicationInfo.FLAG_SYSTEM
}