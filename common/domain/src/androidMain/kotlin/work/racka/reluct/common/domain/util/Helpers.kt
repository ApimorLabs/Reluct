package work.racka.reluct.common.domain.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build

// Used to filter out apps. If an app has a main activity then it is probably not an that we
// need when taking screen time usage.
internal fun hasMainActivity(context: Context, packageName: String): Boolean {
    val packageManager = context.packageManager
    return packageManager.getLaunchIntentForPackage(packageName) != null
}

/**
 * This is not reliable. Just check for the mainActivity like shown above!
 */
internal fun hasSystemFlag(
    context: Context,
    appInfo: ApplicationInfo? = null,
    packageName: String
): Boolean = if (appInfo != null) {
    appInfo.flags == ApplicationInfo.FLAG_SYSTEM
} else {
    val applicationInfo = context.packageManager.run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getApplicationInfo(
                packageName,
                PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
            )
        } else {
            @Suppress("DEPRECATION")
            getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        }
    }
    applicationInfo.flags == ApplicationInfo.FLAG_SYSTEM
}
