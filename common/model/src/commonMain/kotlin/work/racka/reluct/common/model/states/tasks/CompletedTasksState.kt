package work.racka.reluct.common.model.states.tasks

import work.racka.reluct.common.model.domain.tasks.Task

sealed class CompletedTasksState(
    val tasksData: Map<String, List<Task>>,
    val shouldUpdateData: Boolean,
) {
    data class Data(
        private val tasks: Map<String, List<Task>> = mapOf(),
        private val newDataPresent: Boolean = true,
    ) : CompletedTasksState(tasks, newDataPresent)

    class Loading(
        tasks: Map<String, List<Task>> = mapOf(),
        newDataPresent: Boolean = true,
    ) : CompletedTasksState(tasks, newDataPresent)
}
