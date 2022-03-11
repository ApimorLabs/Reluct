package work.racka.reluct.common.model.data.local.task

data class TaskDbObject(
    val id: Long,
    val title: String,
    val description: String?,
    val done: Boolean,
    val overdue: Boolean,
    val dueDateLocalDateTime: String,
    val reminderLocalDateTime: String?,
    val timeZoneId: String
)
