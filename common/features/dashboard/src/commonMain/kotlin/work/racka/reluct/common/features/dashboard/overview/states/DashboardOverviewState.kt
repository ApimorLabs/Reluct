package work.racka.reluct.common.features.dashboard.overview.states

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import work.racka.reluct.common.model.domain.goals.Goal
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.domain.usagestats.UsageStats

data class DashboardOverviewState(
    val todayScreenTimeState: TodayScreenTimeState = TodayScreenTimeState.Nothing,
    val todayTasksState: TodayTasksState = TodayTasksState.Nothing,
    val goals: ImmutableList<Goal> = persistentListOf()
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
    val pending: ImmutableList<Task>
) {
    data class Data(private val tasks: ImmutableList<Task>) : TodayTasksState(pending = tasks)
    class Loading(tasks: ImmutableList<Task> = persistentListOf()) :
        TodayTasksState(pending = tasks)

    object Nothing : TodayTasksState(pending = persistentListOf())
}
