package work.racka.reluct.common.data.alarms

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import work.racka.reluct.common.data.R
import work.racka.reluct.common.system_service.notifications.NotificationChannelInfo

object AlarmReminderNotifications {

    fun Context.getTaskReminderNotificationInfo() = NotificationChannelInfo(
        name = getString(R.string.task_reminder_notif_name),
        description = getString(R.string.task_reminder_notif_description),
        channelId = "tasks_reminders",
        importance = NotificationManagerCompat.IMPORTANCE_HIGH
    )
}