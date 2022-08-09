package work.racka.reluct.common.features.screen_time.services

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import work.racka.reluct.common.features.screen_time.R
import work.racka.reluct.common.features.screen_time.permissions.checkUsageAccessPermissions
import work.racka.reluct.common.model.domain.core.Icon
import work.racka.reluct.common.system_service.notifications.NotificationData
import work.racka.reluct.common.system_service.notifications.SimpleAndroidNotification
import work.racka.reluct.common.system_service.notifications.default_channels.getAppAlertsChannel

internal class AndroidScreenTimeServices(private val context: Context) : ScreenTimeServices {

    override fun startLimitsService() {
        if (checkUsageAccessPermissions(context)) {
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.cancel(NOTIFICATION_TAG, NOTIFICATION_ID)
            val intent = Intent(context, ScreenTimeLimitService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else context.startService(intent)
        } else {
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
            val androidNotification = SimpleAndroidNotification(
                context = context,
                notificationData = notificationData,
                channelInfo = getAppAlertsChannel(context),
                onNotificationClick = { context.openDeviceUsageSettings() }
            )
            androidNotification.show()
        }
    }

    override fun stopLimitsService() {
        val intent = Intent(context, ScreenTimeLimitService::class.java)
        context.stopService(intent)
    }

    private fun Context.openDeviceUsageSettings(): PendingIntent {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        return PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        const val NOTIFICATION_ID = 20
        const val NOTIFICATION_TAG = "no_usage_access"
    }
}