package work.racka.reluct.common.features.screen_time.di

import kotlinx.coroutines.CoroutineScope
import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.common.mvvm.koin.vm.commonViewModel
import work.racka.reluct.common.features.screen_time.statistics.ScreenTimeStats
import work.racka.reluct.common.features.screen_time.statistics.ScreenTimeStatsImpl
import work.racka.reluct.common.features.screen_time.statistics.ScreenTimeStatsNewVM

object ScreenTime {
    fun KoinApplication.screenTimeModules() = this.apply {
        modules(commonModule(), Platform.installModule())
    }

    private fun commonModule() = module {
        // TODO: Remove this and its ViewModel
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