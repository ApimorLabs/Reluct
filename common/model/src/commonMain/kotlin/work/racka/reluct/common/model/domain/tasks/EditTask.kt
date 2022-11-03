package work.racka.reluct.common.model.domain.tasks

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class EditTask(
    val id: String,
    val title: String,
    val description: String?,
    val done: Boolean,
    val overdue: Boolean,
    val taskLabels: ImmutableList<TaskLabel> = persistentListOf(),
    val dueDateLocalDateTime: String,
    val completedLocalDateTime: String?,
    val reminderLocalDateTime: String?,
    val timeZoneId: String,
)
