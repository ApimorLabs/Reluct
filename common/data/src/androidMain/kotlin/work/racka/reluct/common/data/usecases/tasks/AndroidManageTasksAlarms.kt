package work.racka.reluct.common.data.usecases.tasks

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import work.racka.reluct.common.data.alarms.AlarmsKeys
import work.racka.reluct.common.data.alarms.AlarmsReceiver

internal class AndroidManageTasksAlarms(private val context: Context) : ManageTasksAlarms {

    override suspend fun setAlarm(taskId: String, dateTime: LocalDateTime) {
        val alarmManager = getAlarmManager(context)
        if (canScheduleAlarms(alarmManager)) {
            val intent = Intent(context, AlarmsReceiver::class.java).apply {
                action = AlarmsKeys.TASK_REMINDER.action
                putExtra(AlarmsKeys.TASK_REMINDER.key, taskId)
            }
            val pendingIntent = context.alarmPendingIntent(intent)
            val timeInMillis =
                dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            val currentTimeMillis = Clock.System.now().toEpochMilliseconds()
            // Terminate execution because the time being set has already passed
            if (currentTimeMillis >= timeInMillis) return

            // Continue setting the Alarm
            alarmManager.run {
                cancel(pendingIntent)
                setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            }
        } else {
            println("Reluct: Can't schedule Alarm. Permission missing!")
        }
    }

    override suspend fun removeAlarm(taskId: String) {
        val intent = Intent(context, AlarmsReceiver::class.java).apply {
            action = AlarmsKeys.TASK_REMINDER.action
            putExtra(AlarmsKeys.TASK_REMINDER.key, taskId)
        }
        val pendingIntent = context.alarmPendingIntent(intent)
        val alarmManager = getAlarmManager(context)
        alarmManager.cancel(pendingIntent)
    }

    private fun Context.alarmPendingIntent(intent: Intent) = PendingIntent
        .getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    private fun getAlarmManager(context: Context): AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private fun canScheduleAlarms(alarm: AlarmManager) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) alarm.canScheduleExactAlarms() else true
}