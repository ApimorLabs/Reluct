package work.racka.reluct.common.features.screen_time.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import work.racka.reluct.common.data.usecases.app_usage.GetDailyUsageStats
import work.racka.reluct.common.data.usecases.app_usage.GetWeeklyUsageStats
import work.racka.reluct.common.data.usecases.time.GetWeekRangeFromOffset
import work.racka.reluct.common.features.screen_time.statistics.ScreenTimeStats
import work.racka.reluct.common.features.screen_time.statistics.ScreenTimeStatsImpl

actual class ScreenTimeStatsViewModel(
    getWeeklyUsageStats: GetWeeklyUsageStats,
    getDailyUsageStats: GetDailyUsageStats,
    getWeekRangeFromOffset: GetWeekRangeFromOffset
) : ViewModel() {
    actual val host: ScreenTimeStats by lazy {
        ScreenTimeStatsImpl(
            getWeeklyUsageStats = getWeeklyUsageStats,
            getDailyUsageStats = getDailyUsageStats,
            getWeekRangeFromOffset = getWeekRangeFromOffset,
            scope = viewModelScope
        )
    }
}