package work.racka.reluct.common.data.alarms

internal object AlarmsKeys {
    val TASK_REMINDER = IntentData(action = "TASK_REMINDER_ACTION", key = "TASK_REMINDER_KEY")
}

internal class IntentData(val action: String, val key: String)