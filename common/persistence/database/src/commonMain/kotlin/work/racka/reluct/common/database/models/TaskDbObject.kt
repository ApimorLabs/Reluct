package work.racka.reluct.common.database.models

/**
 * We don't store time as Instant to avoid changes in TimeZone rules
 * It's best to store time as LocalDateTime then convert it to the correct
 * TimeZone if it changes later on.
 */
data class TaskDbObject(
    val id: String,
    val title: String,
    val description: String?,
    val done: Boolean,
    val overdue: Boolean,
    val dueDateLocalDateTime: String,
    val completedLocalDateTime: String?,
    val reminderLocalDateTime: String?,
    val timeZoneId: String,
)
