package work.racka.reluct.common.model.states.tasks

import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.domain.tasks.TasksStatistics

sealed class TaskStatisticsState {
    data class Data(
        val tasksStatistics: TasksStatistics,
        val completedTasks: List<Task> = listOf(),
        val pendingTasks: List<Task> = listOf(),
    ) : TaskStatisticsState()

    object Loading : TaskStatisticsState()
}
