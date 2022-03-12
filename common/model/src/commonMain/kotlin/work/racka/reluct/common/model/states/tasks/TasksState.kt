package work.racka.reluct.common.model.states.tasks

import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.domain.tasks.Task

sealed class TasksState {
    data class PendingTasks(
        val tasks: Map<String, List<Task>> = mapOf(),
        val overdueTasks: List<Task> = listOf()
    ) : TasksState()

    data class CompletedTasks(
        val tasks: Map<String, List<Task>> = mapOf()
    ) : TasksState()

    data class Statistics(
        val completedTasks: List<Task> = listOf(),
        val pendingTasks: List<Task> = listOf()
    ) : TasksState()

    data class AddEditTask(
        val task: EditTask? = null
    )

    object Loading : TasksState()

    companion object {
        val EmptyPendingTasks = PendingTasks()
        val EmptyCompletedTasks = CompletedTasks()
        val EmptyStatistics = Statistics()
    }
}
