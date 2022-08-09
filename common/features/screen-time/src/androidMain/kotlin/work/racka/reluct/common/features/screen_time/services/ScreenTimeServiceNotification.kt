package work.racka.reluct.common.features.screen_time.services

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import work.racka.reluct.common.features.screen_time.R
import work.racka.reluct.common.system_service.notifications.NotificationChannelInfo
import work.racka.reluct.common.system_service.notifications.createNotificationChannel

object ScreenTimeServiceNotification {
    fun createNotification(
        context: Context,
        title: String,
        content: String,
        onNotificationClick: () -> PendingIntent?
    ): Notification {
        val notificationManager = NotificationManagerCompat.from(context)
        val channelInfo = context.screenTimeChannelInfo
        channelInfo.createNotificationChannel(notificationManager)
        val builder = NotificationCompat.Builder(context, channelInfo.channelId).apply {
            setSmallIcon(R.drawable.ic_twotone_aod_24)
            setContentTitle(title)
            setContentText(content)
            priority = NotificationCompat.PRIORITY_LOW
            onNotificationClick()?.let { intent ->
                setContentIntent(intent)
            }
            setAutoCancel(false)
            setCategory(NotificationCompat.CATEGORY_STATUS)
            setTicker(title)
        }
        return builder.build()
    }

    private val Context.screenTimeChannelInfo
        get() = NotificationChannelInfo(
            name = getString(R.string.screen_time_service_channel_name),
            description = getString(R.string.screen_time_service_channel_desc),
            channelId = "screen_time_limit_service",
            importance = NotificationManagerCompat.IMPORTANCE_LOW
        )

    const val NOTIFICATION_ID = 2143
}