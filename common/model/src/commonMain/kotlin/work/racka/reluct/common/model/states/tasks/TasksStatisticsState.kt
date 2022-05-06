package work.racka.reluct.common.model.states.tasks

import work.racka.reluct.common.model.domain.tasks.DailyTasksStats
import work.racka.reluct.common.model.util.time.Week

data class TasksStatisticsState(
    val weekOffset: Int = 0,
    val selectedDay: Int = 0,
    val weeklyTasksState: WeeklyTasksState = WeeklyTasksState.Loading,
    val dailyTasksState: DailyTasksState = DailyTasksState.Loading,
)

sealed class WeeklyTasksState {
    data class Data(
        val weeklyTasks: Map<Week, DailyTasksStats>,
    ) : WeeklyTasksState()

    object Loading : WeeklyTasksState()

    object Empty : WeeklyTasksState()
}

sealed class DailyTasksState {
    data class Data(
        val dailyTasks: DailyTasksStats,
    ) : DailyTasksState()

    object Loading : DailyTasksState()

    object Empty : DailyTasksState()
}