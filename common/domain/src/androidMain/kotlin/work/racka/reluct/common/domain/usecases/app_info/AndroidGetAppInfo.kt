package work.racka.reluct.common.domain.usecases.app_info

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import work.racka.reluct.common.app.usage.stats.R
import work.racka.reluct.common.model.domain.core.Icon

internal class AndroidGetAppInfo(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GetAppInfo {

    override suspend fun getAppIcon(packageName: String): Icon = withContext(dispatcher) {
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
        Icon(appIcon)
    }

    override suspend fun getAppName(packageName: String): String = withContext(dispatcher) {
        var appName = packageName
        try {
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
            appName = packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            println("Package Name not found: $packageName")
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
        appName
    }
}