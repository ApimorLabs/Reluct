package work.racka.reluct.common.data.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class TasksAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == AlarmsKeys.TASK_REMINDER.action) {
            val taskId = intent.getStringExtra(AlarmsKeys.TASK_REMINDER.key)
            Toast.makeText(context, taskId, Toast.LENGTH_LONG).show()
        }
    }
}