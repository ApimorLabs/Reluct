package work.racka.reluct.common.features.screen_time.statistics.states.app_stats

import work.racka.reluct.common.features.screen_time.statistics.states.ScreenTimeStatsSelectedInfo
import work.racka.reluct.common.model.domain.limits.AppTimeLimit
import work.racka.reluct.common.model.domain.usagestats.AppUsageStats

data class AppScreenTimeStatsState(
    val selectedInfo: ScreenTimeStatsSelectedInfo = ScreenTimeStatsSelectedInfo(),
    val weeklyData: WeeklyAppUsageStatsState = WeeklyAppUsageStatsState.Empty,
    val dailyData: DailyAppUsageStatsState = DailyAppUsageStatsState.Empty,
    val appSettingsState: AppSettingsState = AppSettingsState.Nothing
)

sealed class WeeklyAppUsageStatsState(
    val usageStats: List<AppUsageStats>,
    val formattedTotalTime: String
) {
    data class Data(
        private val weeklyUsageStats: List<AppUsageStats>,
        private val weeklyFormattedTotalTime: String
    ) : WeeklyAppUsageStatsState(weeklyUsageStats, weeklyFormattedTotalTime)

    class Loading(
        weeklyUsageStats: List<AppUsageStats> = listOf(),
        weeklyFormattedTotalTime: String = "..."
    ) : WeeklyAppUsageStatsState(weeklyUsageStats, weeklyFormattedTotalTime)

    object Empty : WeeklyAppUsageStatsState(listOf(), "...")
}

sealed class DailyAppUsageStatsState {
    data class Data(
        val usageStat: AppUsageStats,
        val dayText: String
    ) : DailyAppUsageStatsState()

    object Loading : DailyAppUsageStatsState()

    object Empty : DailyAppUsageStatsState()
}

sealed class AppSettingsState {
    object Nothing : AppSettingsState()
    object Loading : AppSettingsState()
    data class Data(
        val appTimeLimit: AppTimeLimit,
        val isDistractingApp: Boolean
    ) : AppSettingsState()
}