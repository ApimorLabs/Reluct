package work.racka.reluct.common.features.screen_time.di

import kotlinx.coroutines.CoroutineScope
import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.reluct.common.features.screen_time.statistics.ScreenTimeStats
import work.racka.reluct.common.features.screen_time.statistics.ScreenTimeStatsImpl
import work.racka.reluct.common.features.screen_time.statistics.ScreenTimeStatsNewVM
import work.racka.reluct.common.mvvm.koin.vm.commonViewModel

object ScreenTime {
    fun KoinApplication.screenTimeModules() = this.apply {
        modules(commonModule(), Platform.installModule())
    }

    private fun commonModule() = module {
        factory<ScreenTimeStats> { (scope: CoroutineScope) ->
            ScreenTimeStatsImpl(
                getWeeklyUsageStats = get(),
                getDailyUsageStats = get(),
                getWeekRangeFromOffset = get(),
                scope = scope
            )
        }

        commonViewModel {
            ScreenTimeStatsNewVM(
                getWeeklyUsageStats = get(),
                getDailyUsageStats = get(),
                getWeekRangeFromOffset = get()
            )
        }
    }
}