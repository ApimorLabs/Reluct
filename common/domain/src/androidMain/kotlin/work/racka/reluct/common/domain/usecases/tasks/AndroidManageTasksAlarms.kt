package work.racka.reluct.common.domain.usecases.tasks

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.datetime.*
import work.racka.reluct.common.domain.alarms.AlarmsKeys
import work.racka.reluct.common.domain.alarms.AlarmsReceiver

internal class AndroidManageTasksAlarms(
    private val context: Context,
    private val getTasks: GetTasksUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ManageTasksAlarms {

    override suspend fun setAlarm(taskId: String, dateTime: LocalDateTime) {
        val alarmManager = getAlarmManager(context)
        if (canScheduleAlarms(alarmManager)) {
            Log.d(TAG, "Setting Alarm")
            val pendingIntent = context.makeAlarmPendingIntent(taskId)
            val timeInMillis = dateTime
                .toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
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
            Log.d(TAG, "Reluct: Can't schedule Alarms. Permission missing!")
        }
    }

    override suspend fun removeAlarm(taskId: String) {
        val pendingIntent = context.makeAlarmPendingIntent(taskId)
        val alarmManager = getAlarmManager(context)
        alarmManager.cancel(pendingIntent)
    }

    override suspend fun rescheduleAlarms() {
        Log.d(TAG, "Rescheduling Alarms...")
        withContext(dispatcher) {
            val alarmManager = getAlarmManager(context)
            if (canScheduleAlarms(alarmManager)) {
                val currentTimeMillis = Clock.System.now().toEpochMilliseconds()

                // Get 10,000 pending task if available
                val tasksToSchedule = getTasks.getPendingTasks(10, 1000).first()
                    .filter { task -> task.reminderDateAndTime != null }
                    .map { filteredTask ->
                        val timeMillis = filteredTask.reminderDateAndTime!!.toLocalDateTime()
                            .toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
                        filteredTask.id to timeMillis
                    }

                tasksToSchedule.forEach { taskTimePair ->
                    if (currentTimeMillis < taskTimePair.second) {
                        val pendingIntent = context.makeAlarmPendingIntent(taskTimePair.first)
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            taskTimePair.second,
                            pendingIntent
                        )
                    }
                }
            } else {
                Log.d(TAG, "Reluct: Can't schedule Alarms. Permission missing!")
            }
        }
    }

    private fun Context.makeAlarmPendingIntent(taskId: String): PendingIntent {
        val intent = Intent(context, AlarmsReceiver::class.java).apply {
            action = AlarmsKeys.TASK_REMINDER.action
            putExtra(AlarmsKeys.TASK_REMINDER.key, taskId)
        }
        return PendingIntent
            .getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
    }

    private fun getAlarmManager(context: Context): AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private fun canScheduleAlarms(alarm: AlarmManager) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) alarm.canScheduleExactAlarms() else true

    companion object {
        const val TAG = "AndroidManageTasksAlarms"
    }
}