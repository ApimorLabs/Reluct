package work.racka.reluct.common.model.states.tasks

import work.racka.reluct.common.model.domain.tasks.Task

sealed class PendingTasksState {
    data class Data(
        val tasks: Map<String, List<Task>> = mapOf(),
        val overdueTasks: List<Task> = listOf(),
    ) : PendingTasksState()

    object Loading : PendingTasksState()
}
