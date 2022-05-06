package work.racka.reluct.common.model.states.tasks

import work.racka.reluct.common.model.domain.tasks.Task

data class TasksStatisticsState(
    val weekOffset: Int = 0,
    val selectedDay: Int = 0,
    val weeklyTasksState: WeeklyTasksState = WeeklyTasksState.Loading,
    val dailyTasksState: DailyTasksState = DailyTasksState.Loading,
)

sealed class WeeklyTasksState {
    data class Data(
        val tasksCompleted: Int,
        val tasksPending: Int,
    ) : WeeklyTasksState()

    object Loading : WeeklyTasksState()

    object Empty : WeeklyTasksState()
}

sealed class DailyTasksState {
    data class Data(
        val dayCompletedTasks: List<Task>,
        val dayPendingTasks: List<Task>,
    ) : DailyTasksState()

    object Loading : DailyTasksState()

    object Empty : DailyTasksState()
}