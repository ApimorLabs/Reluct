package work.racka.reluct.common.features.screen_time.statistics.states.all_stats

import work.racka.reluct.common.features.screen_time.limits.states.AppTimeLimitState
import work.racka.reluct.common.features.screen_time.statistics.states.ScreenTimeStatsSelectedInfo
import work.racka.reluct.common.model.domain.usagestats.UsageStats
import work.racka.reluct.common.model.util.time.Week

data class ScreenTimeStatsState(
    val selectedInfo: ScreenTimeStatsSelectedInfo = ScreenTimeStatsSelectedInfo(),
    val weeklyData: WeeklyUsageStatsState = WeeklyUsageStatsState.Empty,
    val dailyData: DailyUsageStatsState = DailyUsageStatsState.Empty,
    val appTimeLimit: AppTimeLimitState = AppTimeLimitState.Nothing
)

sealed class WeeklyUsageStatsState(
    val usageStats: Map<Week, UsageStats>,
    val formattedTotalTime: String
) {
    data class Data(
        private val weeklyUsageStats: Map<Week, UsageStats>,
        private val weeklyFormattedTotalTime: String
    ) : WeeklyUsageStatsState(weeklyUsageStats, weeklyFormattedTotalTime)

    class Loading(
        weeklyUsageStats: Map<Week, UsageStats> = mapOf(),
        weeklyFormattedTotalTime: String = "..."
    ) : WeeklyUsageStatsState(weeklyUsageStats, weeklyFormattedTotalTime)

    object Empty : WeeklyUsageStatsState(mapOf(), "...")
}

sealed class DailyUsageStatsState(
    val usageStat: UsageStats,
    val dayText: String
) {
    data class Data(
        private val dailyUsageStats: UsageStats
    ) : DailyUsageStatsState(dailyUsageStats, dailyUsageStats.dateFormatted)

    class Loading(
        dailyUsageStats: UsageStats = UsageStats()
    ) : DailyUsageStatsState(dailyUsageStats, dailyUsageStats.dateFormatted)

    object Empty : DailyUsageStatsState(UsageStats(), "...")
}
