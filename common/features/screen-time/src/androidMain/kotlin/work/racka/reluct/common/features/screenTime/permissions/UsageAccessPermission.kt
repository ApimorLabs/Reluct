package work.racka.reluct.common.features.screenTime.permissions

import android.app.AppOpsManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Process
import android.provider.Settings
import androidx.core.app.NotificationCompat
import work.racka.reluct.common.features.screenTime.R
import work.racka.reluct.common.model.domain.core.Icon
import work.racka.reluct.common.services.notifications.NotificationData
import work.racka.reluct.common.services.notifications.SimpleAndroidNotification
import work.racka.reluct.common.services.notifications.defaultChannels.getAppAlertsChannel

internal object UsageAccessPermission {

    fun isAllowed(
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

    fun requestUsageAccessNotification(context: Context): SimpleAndroidNotification {
        val drawable = context.getDrawable(R.drawable.ic_twotone_report_24)
        val icon = drawable?.let { Icon(it) }
        val notificationData = NotificationData(
            iconProvider = icon,
            title = context.getString(R.string.no_usage_access_notif_title),
            content = context.getString(R.string.no_usage_access_notif_content),
            notificationId = NOTIFICATION_ID,
            notificationTag = NOTIFICATION_TAG,
            category = NotificationCompat.CATEGORY_REMINDER
        )
        return SimpleAndroidNotification(
            context = context,
            notificationData = notificationData,
            channelInfo = getAppAlertsChannel(context),
            onNotificationClick = { context.openDeviceUsageSettings() }
        )
    }

    private fun requestIntent() = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)

    private fun Context.openDeviceUsageSettings(): PendingIntent = PendingIntent.getActivity(
        this,
        0,
        requestIntent(),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    private const val NOTIFICATION_ID = 20
    private const val NOTIFICATION_TAG = "no_usage_access"
}
