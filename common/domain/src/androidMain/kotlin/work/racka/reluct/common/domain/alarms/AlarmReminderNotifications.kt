package work.racka.reluct.common.domain.alarms

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import work.racka.reluct.common.domain.R
import work.racka.reluct.common.system_service.notifications.NotificationChannelInfo

internal object AlarmReminderNotifications {

    fun Context.getTaskReminderNotificationInfo() = NotificationChannelInfo(
        name = getString(R.string.task_reminder_notif_name),
        description = getString(R.string.task_reminder_notif_description),
        channelId = "tasks_reminders",
        importance = NotificationManagerCompat.IMPORTANCE_HIGH
    )

    fun openNotificationPendingIntent(context: Context, uriString: String): PendingIntent {
        val intent = Intent(
            Intent.ACTION_VIEW,
            uriString.toUri()
        )
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}