package work.racka.reluct.common.features.screen_time.statistics.states

import work.racka.reluct.common.model.domain.usagestats.UsageStats
import work.racka.reluct.common.model.util.time.Week

data class ScreenTimeStatsState(
    val weekOffset: Int = 0,
    val selectedWeekText: String = "...",
    val selectedDay: Int = 0,
    val weeklyData: WeeklyUsageStatsState = WeeklyUsageStatsState.Empty,
    val dailyData: DailyUsageStatsState = DailyUsageStatsState.Empty
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
