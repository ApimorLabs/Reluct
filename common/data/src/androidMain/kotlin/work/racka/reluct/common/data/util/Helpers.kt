package work.racka.reluct.common.data.util

import android.content.Context

// Check if it is not a system app
internal fun isSystemApp(context: Context, packageName: String): Boolean {
    val packageManager = context.packageManager
    return packageManager.getLaunchIntentForPackage(packageName) == null
}