package work.racka.reluct.common.model.domain.tasks

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DailyTasksStats(
    val dateFormatted: String = "...",
    val completedTasks: ImmutableList<Task> = persistentListOf(),
    val pendingTasks: ImmutableList<Task> = persistentListOf(),
) {
    val completedTasksCount
        get() = completedTasks.size
    val pendingTasksCount
        get() = pendingTasks.size
}
