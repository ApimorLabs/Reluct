package work.racka.reluct.common.features.screen_time.di

import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.common.mvvm.koin.vm.commonViewModel
import work.racka.reluct.common.features.screen_time.limits.ScreenTimeLimitsViewModel
import work.racka.reluct.common.features.screen_time.statistics.ScreenTimeStatsViewModel

object ScreenTime {
    fun KoinApplication.screenTimeModules() = this.apply {
        modules(commonModule(), Platform.installModule())
    }

    private fun commonModule() = module {
        commonViewModel {
            ScreenTimeStatsViewModel(
                getWeeklyUsageStats = get(),
                getDailyUsageStats = get(),
                getWeekRangeFromOffset = get()
            )
        }

        commonViewModel {
            ScreenTimeLimitsViewModel(
                manageFocusMode = get(),
                managePausedApps = get(),
                manageDistractingApps = get()
            )
        }
    }
}