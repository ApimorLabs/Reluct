package work.racka.reluct.utils

import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Process
import timber.log.Timber
import work.racka.reluct.data.local.usagestats.AppUsageInfo
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object Utils {

    fun checkUsageAccessPermissions(
        context: Context
    ): Boolean {
        val appOps: AppOpsManager = context.getSystemService(Context.APP_OPS_SERVICE)
                as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
        } else {
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    fun getFormattedTime(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1)
        return when {
            hours > 0 -> {
                String.format(
                    "%02d hrs %02d min",
                    hours,
                    minutes
                )
            }
            minutes <= 1 -> {
                "Less than 1 minute"
            }
            else -> {
                "$minutes min"
            }
        }
    }

    fun getFormattedDate(date: Date): String {
        val dateFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun sortByHighestForegroundTime(stats: Map<String, UsageStats>?): Map<String, UsageStats>? {
        if (stats == null) return null
        return stats.toList().sortedBy { (_, value) ->
            value.totalTimeInForeground
        }.reversed()
            .toMap()
    }

    fun sortByHighestForegroundTime(appUsageInfo: Collection<AppUsageInfo>): List<AppUsageInfo> {
        return appUsageInfo.sortedByDescending { appInfo ->
            appInfo.timeInForeground
        }
    }

    fun getAppIcon(packageName: String, context: Context): Drawable? {
        var appIcon: Drawable? = null
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
        return appIcon
    }
}