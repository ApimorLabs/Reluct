package work.racka.reluct.common.network.model

import work.racka.reluct.common.database.models.TaskDbObject
import work.racka.reluct.common.database.models.TaskLabelDbObject

data class TaskNetworkObject(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val done: Boolean = false,
    val overdue: Boolean = false,
    val taskLabels: List<TaskLabelNetworkObject> = listOf(),
    val dueDateLocalDateTime: String? = null,
    val completedLocalDateTime: String? = null,
    val reminderLocalDateTime: String? = null,
    val timeZoneId: String? = null,
) {
    fun toDbObject(): TaskDbObject? =
        if (id == null || title == null || dueDateLocalDateTime == null || timeZoneId == null) {
            null
        } else {
            TaskDbObject(
                id = id,
                title = title,
                description = description,
                done = done,
                overdue = overdue,
                taskLabels = taskLabels.mapNotNull { it.toDbObject() },
                dueDateLocalDateTime = dueDateLocalDateTime,
                completedLocalDateTime = completedLocalDateTime,
                reminderLocalDateTime = reminderLocalDateTime,
                timeZoneId = timeZoneId
            )
        }
}

fun TaskDbObject.toNetworkObject() = TaskNetworkObject(
    id = id,
    title = title,
    description = description,
    done = done,
    overdue = overdue,
    taskLabels = taskLabels.map { it.toNetworkObject() },
    dueDateLocalDateTime = dueDateLocalDateTime,
    completedLocalDateTime = completedLocalDateTime,
    reminderLocalDateTime = reminderLocalDateTime,
    timeZoneId = timeZoneId
)
