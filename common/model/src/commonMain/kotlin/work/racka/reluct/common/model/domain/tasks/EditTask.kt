package work.racka.reluct.common.model.domain.tasks

data class EditTask(
    val id: Long,
    val title: String,
    val description: String?,
    val done: Boolean,
    val overdue: Boolean,
    val dueDateLocalDateTime: String,
    val completedLocalDateTime: String?,
    val reminderLocalDateTime: String?,
    val timeZoneId: String
)
