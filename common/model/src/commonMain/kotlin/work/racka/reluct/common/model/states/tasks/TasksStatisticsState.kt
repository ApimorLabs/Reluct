package work.racka.reluct.common.model.states.tasks

import work.racka.reluct.common.model.domain.tasks.DailyTasksStats
import work.racka.reluct.common.model.util.time.Week

data class TasksStatisticsState(
    val weekOffset: Int = 0,
    val selectedWeekText: String = "",
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

sealed class DailyTasksState(
    val dailyTasks: DailyTasksStats,
    val dayText: String,
) {
    data class Data(
        val tasks: DailyTasksStats,
        val dayTextValue: String = "...",
    ) : DailyTasksState(dailyTasks = tasks, dayText = dayTextValue)

    object Loading : DailyTasksState(dailyTasks = DailyTasksStats(), dayText = "...")

    object Empty : DailyTasksState(dailyTasks = DailyTasksStats(), dayText = "...")
}