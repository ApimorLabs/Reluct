package work.racka.reluct.common.model.states.tasks

import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.domain.tasks.TasksStatistics

sealed class TaskStatisticsState {
    data class Data(
        val tasksStatistics: TasksStatistics,
        val dayCompletedTasks: List<Task> = listOf(),
        val dayPendingTasks: List<Task> = listOf(),
    ) : TaskStatisticsState()

    object Loading : TaskStatisticsState()
}
