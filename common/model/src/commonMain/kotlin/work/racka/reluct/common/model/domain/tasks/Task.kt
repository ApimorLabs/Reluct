package work.racka.reluct.common.model.domain.tasks

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class Task(
    val id: String,
    val title: String,
    val description: String,
    val done: Boolean,
    val overdue: Boolean,
    val taskLabels: ImmutableList<TaskLabel> = persistentListOf(),
    val dueDate: String,
    val dueTime: String,
    val timeLeftLabel: String,
    val reminderFormatted: String,
    val reminderDateAndTime: String? = null,
    val completedDateAndTime: String,
)
