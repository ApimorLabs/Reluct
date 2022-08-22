package work.racka.reluct.common.features.dashboard.di

import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.common.mvvm.koin.vm.commonViewModel
import work.racka.reluct.common.features.dashboard.overview.DashboardOverviewViewModel

object Dashboard {
    fun KoinApplication.install() = apply {
        modules(commonModule())
    }

    private fun commonModule() = module {
        commonViewModel {
            DashboardOverviewViewModel(
                getTasksUseCase = get(),
                modifyTasksUsesCase = get(),
                getDailyUsageStats = get()
            )
        }
    }
}