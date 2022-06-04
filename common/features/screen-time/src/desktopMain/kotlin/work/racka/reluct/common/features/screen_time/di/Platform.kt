package work.racka.reluct.common.features.screen_time.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.features.screen_time.viewmodels.ScreenTimeStatsViewModel

internal actual object Platform {
    actual fun installModule(): Module = module {
        factory {
            val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)
            ScreenTimeStatsViewModel(
                getWeeklyUsageStats = get(),
                getDailyUsageStats = get(),
                getWeekRangeFromOffset = get(),
                scope = viewModelScope
            )
        }
    }
}