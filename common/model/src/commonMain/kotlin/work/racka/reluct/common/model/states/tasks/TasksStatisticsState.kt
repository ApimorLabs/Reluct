package work.racka.reluct.common.model.states.tasks

import work.racka.reluct.common.model.domain.tasks.DailyTasksStats
import work.racka.reluct.common.model.util.time.Week

data class TasksStatisticsState(
    val weekOffset: Int = 0,
    val selectedWeekText: String = "...",
    val selectedDay: Int = 0,
    val weeklyTasksState: WeeklyTasksState = WeeklyTasksState.Loading(),
    val dailyTasksState: DailyTasksState = DailyTasksState.Loading(),
)

sealed class WeeklyTasksState(
    val weeklyTasks: Map<Week, DailyTasksStats> = mapOf(),
    val totalWeekTasksCount: Int = 0,
) {
    data class Data(
        val tasks: Map<Week, DailyTasksStats>,
        val totalTaskCount: Int,
    ) : WeeklyTasksState(tasks, totalTaskCount)

    class Loading(
        tasks: Map<Week, DailyTasksStats> = mapOf(),
        totalTaskCount: Int = 0,
    ) : WeeklyTasksState(tasks, totalTaskCount)

    class Empty(
        tasks: Map<Week, DailyTasksStats>,
        totalTaskCount: Int = 0,
    ) : WeeklyTasksState(tasks, totalTaskCount)
}

sealed class DailyTasksState(
    val dailyTasks: DailyTasksStats,
    val dayText: String,
) {
    data class Data(
        val tasks: DailyTasksStats,
        val dayTextValue: String = tasks.dateFormatted,
    ) : DailyTasksState(dailyTasks = tasks, dayText = dayTextValue)

    class Loading(
        tasks: DailyTasksStats = DailyTasksStats(),
        dayTextValue: String = "...",
    ) : DailyTasksState(dailyTasks = tasks, dayText = dayTextValue)

    class Empty(
        tasks: DailyTasksStats = DailyTasksStats(),
        dayTextValue: String = "...",
    ) : DailyTasksState(dailyTasks = tasks, dayText = dayTextValue)
}