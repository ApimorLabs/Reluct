package work.racka.reluct.common.app.usage.stats.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import timber.log.Timber
import work.racka.reluct.common.app.usage.stats.R
import work.racka.reluct.common.model.domain.core.Icon

internal fun getAppIcon(context: Context, packageName: String): Icon {
    var appIcon: Drawable = context.resources.getDrawable(R.drawable.default_app_icon, null)
    try {
        Timber.d("PackageName: $packageName")
        val newContext =
            context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY)
        appIcon = newContext.packageManager.getApplicationIcon(packageName)
    } catch (e: PackageManager.NameNotFoundException) {
        Timber.d("Package Name not found")
    } catch (e: Exception) {
        Timber.d("Error: ${e.message}")
    }
    return Icon(appIcon)
}

// If application name can't be found the package name will be returned
internal fun getAppName(context: Context, packageName: String): String {
    var appName = packageName
    try {
        Timber.d("PackageName: $packageName")
        val newContext =
            context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY)
        val packageManager = newContext.packageManager
        val appInfo = packageManager.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getApplicationInfo(
                    packageName,
                    PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
                )
            } else {
                @Suppress("DEPRECATION")
                packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            }
        }
        appName = packageManager.getApplicationLabel(appInfo).toString()
    } catch (e: PackageManager.NameNotFoundException) {
        Timber.d("Package Name not found")
    } catch (e: Exception) {
        Timber.d("Error: ${e.message}")
    }
    return appName
}

// Check if it is not a system app
internal fun isSystemApp(context: Context, packageName: String): Boolean {
    val newContext =
        context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY)
    val packageManager = newContext.packageManager
    val appInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getApplicationInfo(
            packageName,
            PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
        )
    } else {
        @Suppress("DEPRECATION")
        packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
    }
    return appInfo.flags == ApplicationInfo.FLAG_SYSTEM
}

/*
internal fun getDominantAppIconColor(appIcon: Drawable?): Int {
    val defaultColor = Color.GRAY
    val palette = appIcon?.let {
        Palette
            .from(it.toBitmap())
            .generate()
    }
    return palette?.getVibrantColor(defaultColor) ?: defaultColor
}*/
