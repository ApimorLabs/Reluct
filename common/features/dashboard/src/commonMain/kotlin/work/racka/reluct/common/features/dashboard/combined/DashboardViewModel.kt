package work.racka.reluct.common.features.dashboard.combined

import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.features.dashboard.overview.DashboardOverviewViewModel
import work.racka.reluct.common.features.screen_time.statistics.ScreenTimeStatsViewModel
import work.racka.reluct.common.features.tasks.statistics.TasksStatisticsViewModel

class DashboardViewModel(
    val overview: DashboardOverviewViewModel,
    val screenTimeStats: ScreenTimeStatsViewModel,
    val tasksStats: TasksStatisticsViewModel
) : CommonViewModel() {

    override fun destroy() {
        overview.destroy()
        screenTimeStats.destroy()
        tasksStats.destroy()
    }
}