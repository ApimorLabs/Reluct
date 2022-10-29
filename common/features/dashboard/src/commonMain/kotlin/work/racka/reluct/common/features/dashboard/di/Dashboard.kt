package work.racka.reluct.common.features.dashboard.di

import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.common.mvvm.koin.vm.commonViewModel
import work.racka.reluct.common.domain.usecases.app_usage.GetUsageStats
import work.racka.reluct.common.domain.usecases.goals.GetGoals
import work.racka.reluct.common.domain.usecases.goals.ModifyGoals
import work.racka.reluct.common.domain.usecases.limits.ManageAppTimeLimit
import work.racka.reluct.common.domain.usecases.tasks.GetGroupedTasksStats
import work.racka.reluct.common.domain.usecases.tasks.GetTasksUseCase
import work.racka.reluct.common.domain.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.domain.usecases.time.GetWeekRangeFromOffset
import work.racka.reluct.common.features.dashboard.combined.DashboardViewModel
import work.racka.reluct.common.features.dashboard.overview.DashboardOverviewViewModel
import work.racka.reluct.common.features.dashboard.statistics.DashboardStatisticsViewModel
import work.racka.reluct.common.features.screen_time.services.ScreenTimeServices
import work.racka.reluct.common.features.screen_time.statistics.ScreenTimeStatsViewModel
import work.racka.reluct.common.features.tasks.statistics.TasksStatisticsViewModel

object Dashboard {
    fun KoinApplication.install() = apply {
        modules(commonModule())
    }

    private fun commonModule() = module {
        commonViewModel {
            DashboardOverviewViewModel(
                getTasksUseCase = get(),
                modifyTasksUsesCase = get(),
                getUsageStats = get(),
                getGoals = get(),
                modifyGoals = get(),
                screenTimeServices = get()
            )
        }

        commonViewModel {
            DashboardStatisticsViewModel(
                screenTimeStatsViewModel = get(),
                tasksStatsViewModel = get()
            )
        }

        commonViewModel {
            val getTasksUseCase: GetTasksUseCase = get()
            val modifyTasksUsesCase: ModifyTaskUseCase = get()
            val getUsageStats: GetUsageStats = get()
            val getGoals: GetGoals = get()
            val modifyGoals: ModifyGoals = get()
            val screenTimeServices: ScreenTimeServices = get()
            val getWeekRangeFromOffset: GetWeekRangeFromOffset = get()
            val manageAppTimeLimit: ManageAppTimeLimit = get()
            val getGroupedTasksStats: GetGroupedTasksStats = get()

            val dashboardOverview = DashboardOverviewViewModel(
                getTasksUseCase = getTasksUseCase,
                modifyTasksUsesCase = modifyTasksUsesCase,
                getUsageStats = getUsageStats,
                getGoals = getGoals,
                modifyGoals = modifyGoals,
                screenTimeServices = screenTimeServices
            )
            val screenTimeStats = ScreenTimeStatsViewModel(
                getUsageStats = getUsageStats,
                getWeekRangeFromOffset = getWeekRangeFromOffset,
                manageAppTimeLimit = manageAppTimeLimit
            )
            val tasksStats = TasksStatisticsViewModel(
                modifyTasksUsesCase = modifyTasksUsesCase,
                getGroupedTasksStats = getGroupedTasksStats,
                getWeekRangeFromOffset = getWeekRangeFromOffset
            )
            DashboardViewModel(
                overview = dashboardOverview,
                screenTimeStats = screenTimeStats,
                tasksStats = tasksStats
            )
        }
    }
}