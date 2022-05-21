package work.racka.reluct.common.model.states.tasks

import work.racka.reluct.common.model.domain.tasks.Task

sealed class PendingTasksState(
    val tasksData: Map<String, List<Task>>,
    val overdueTasksData: List<Task>,
    val shouldUpdateData: Boolean,
) {
    data class Data(
        val tasks: Map<String, List<Task>> = mapOf(),
        val overdueTasks: List<Task> = listOf(),
        val newDataPresent: Boolean = true,
    ) : PendingTasksState(tasks, overdueTasks, newDataPresent)

    data class Loading(
        val tasks: Map<String, List<Task>> = mapOf(),
        val overdueTasks: List<Task> = listOf(),
        val newDataPresent: Boolean = true,
    ) : PendingTasksState(tasks, overdueTasks, newDataPresent)
}
