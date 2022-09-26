package work.racka.reluct.android.screens.util

import android.app.AlarmManager
import android.app.AppOpsManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Process
import android.provider.Settings
import androidx.annotation.RequiresApi

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
        @Suppress("DEPRECATION")
        appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
    }
    return mode == AppOpsManager.MODE_ALLOWED
}

fun Context.canScheduleAlarms(): Boolean {
    val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        alarm.canScheduleExactAlarms() else true
}

fun Context.areNotificationsEnabled(): Boolean {
    val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        manager.areNotificationsEnabled()
    } else true
}

@RequiresApi(Build.VERSION_CODES.O)
fun Context.openAppNotificationSettings() {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
    startActivity(intent)
}

fun requestUsageAccessPermission(context: Context) {
    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
    context.startActivity(intent)
}