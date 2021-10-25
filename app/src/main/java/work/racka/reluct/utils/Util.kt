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

object Util {
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

    fun getFormattedTime(millis: Long): String = String.format(
        "%02d hrs %02d min",
        TimeUnit.MILLISECONDS.toHours(millis),
        TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1)
    )

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

    fun sortByHighestForegroundTime(appUsageInfo: List<AppUsageInfo>): List<AppUsageInfo> {
        return appUsageInfo.sortedBy { appInfo ->
            appInfo.timeInForeground
        }.reversed()
    }

    fun getAppIcon(packageName: String, context: Context): Drawable? {
        return try {
            val newContext = context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY)
            newContext.packageManager.getApplicationIcon(packageName)
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.d("Package Name not found")
            null
        } catch (e: Exception) {
            Timber.d("Error: ${e.message}")
            null
        }
    }
}