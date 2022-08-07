package work.racka.reluct.common.data.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

internal class AlarmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        /**
         * Check for actions before acting on the [Intent]
         * Each Alarm has a predefined [Intent.setAction] at creating and
         * the actions & keys are present in [AlarmsKeys]
         */
        // Tasks Reminders
        if (intent.action == AlarmsKeys.TASK_REMINDER.action) {
            val taskId = intent.getStringExtra(AlarmsKeys.TASK_REMINDER.key)
            Toast.makeText(context, taskId, Toast.LENGTH_LONG).show()
        }
    }
}