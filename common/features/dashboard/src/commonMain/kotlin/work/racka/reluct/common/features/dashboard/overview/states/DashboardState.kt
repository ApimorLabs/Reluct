package work.racka.reluct.common.features.dashboard.overview.states

import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.domain.usagestats.UsageStats

data class DashboardState(
    val todayScreenTimeState: TodayScreenTimeState = TodayScreenTimeState.Nothing,
    val todayTasksState: TodayTasksState = TodayTasksState.Nothing
)

sealed class TodayScreenTimeState(
    val usageStats: UsageStats
) {
    data class Data(private val dailyUsageStats: UsageStats) :
        TodayScreenTimeState(usageStats = dailyUsageStats)

    class Loading(usageStats: UsageStats = UsageStats()) :
        TodayScreenTimeState(usageStats = usageStats)

    object Nothing : TodayScreenTimeState(UsageStats())
}

sealed class TodayTasksState(
    val pending: List<Task>
) {
    data class Data(private val tasks: List<Task>) : TodayTasksState(pending = tasks)
    class Loading(tasks: List<Task> = emptyList()) : TodayTasksState(pending = tasks)
    object Nothing : TodayTasksState(pending = emptyList())
}
