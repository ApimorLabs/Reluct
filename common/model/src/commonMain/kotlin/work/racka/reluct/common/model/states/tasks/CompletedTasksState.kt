package work.racka.reluct.common.model.states.tasks

import work.racka.reluct.common.model.domain.tasks.Task

sealed class CompletedTasksState(
    val tasksData: Map<String, List<Task>>,
    val shouldUpdateData: Boolean,
) {
    data class Data(
        val tasks: Map<String, List<Task>> = mapOf(),
        val newDataPresent: Boolean,
    ) : CompletedTasksState(tasks, newDataPresent)

    data class Loading(
        val tasks: Map<String, List<Task>> = mapOf(),
        val newDataPresent: Boolean = true,
    ) : CompletedTasksState(tasks, newDataPresent)
}
