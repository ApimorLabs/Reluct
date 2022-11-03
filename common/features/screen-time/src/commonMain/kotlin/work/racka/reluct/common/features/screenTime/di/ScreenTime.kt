package work.racka.reluct.common.features.screenTime.di

import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.common.mvvm.koin.vm.commonViewModel
import work.racka.reluct.common.features.screenTime.limits.ScreenTimeLimitsViewModel
import work.racka.reluct.common.features.screenTime.statistics.AppScreenTimeStatsViewModel
import work.racka.reluct.common.features.screenTime.statistics.ScreenTimeStatsViewModel

object ScreenTime {
    fun KoinApplication.screenTimeModules() = this.apply {
        modules(commonModule(), Platform.installModule())
    }

    private fun commonModule() = module {
        commonViewModel {
            ScreenTimeStatsViewModel(
                getUsageStats = get(),
                getWeekRangeFromOffset = get(),
                manageAppTimeLimit = get()
            )
        }

        commonViewModel {
            ScreenTimeLimitsViewModel(
                manageFocusMode = get(),
                managePausedApps = get(),
                manageDistractingApps = get()
            )
        }

        commonViewModel { (packageName: String) ->
            AppScreenTimeStatsViewModel(
                packageName = packageName,
                getAppUsageInfo = get(),
                manageAppTimeLimit = get(),
                manageDistractingApps = get(),
                managePausedApps = get(),
                getWeekRangeFromOffset = get()
            )
        }
    }
}
