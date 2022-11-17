package work.racka.reluct.common.features.dashboard.di

import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.common.mvvm.koin.vm.commonViewModel
import work.racka.reluct.common.features.dashboard.overview.DashboardOverviewViewModel
import work.racka.reluct.common.features.dashboard.statistics.DashboardStatisticsViewModel

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
    }
}
