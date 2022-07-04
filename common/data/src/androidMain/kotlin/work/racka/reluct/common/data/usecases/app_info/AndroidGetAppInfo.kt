package work.racka.reluct.common.data.usecases.app_info

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import work.racka.reluct.common.app.usage.stats.R
import work.racka.reluct.common.model.domain.usagestats.Icon

class AndroidGetAppInfo(private val context: Context) : GetAppInfo {

    override fun getAppIcon(packageName: String): Icon {
        var appIcon: Drawable = context.resources.getDrawable(R.drawable.default_app_icon, null)
        try {
            val newContext =
                context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY)
            appIcon = newContext.packageManager.getApplicationIcon(packageName)
        } catch (e: PackageManager.NameNotFoundException) {
            println("Package Name not found: $packageName")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
        return Icon(appIcon)
    }

    override fun getAppName(packageName: String): String {
        var appName = packageName
        try {
            val newContext =
                context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY)
            val packageManager = newContext.packageManager
            val appInfo =
                packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            appName = packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            println("Package Name not found: $packageName")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
        return appName
    }
}