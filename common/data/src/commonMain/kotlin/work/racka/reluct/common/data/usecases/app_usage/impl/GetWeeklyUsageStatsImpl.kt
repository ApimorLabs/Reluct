package work.racka.reluct.common.data.usecases.app_usage.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import work.racka.reluct.common.data.usecases.app_usage.GetDailyUsageStats
import work.racka.reluct.common.data.usecases.app_usage.GetWeeklyUsageStats
import work.racka.reluct.common.model.domain.usagestats.UsageStats
import work.racka.reluct.common.model.util.time.Week

internal class GetWeeklyUsageStatsImpl(
    private val dailyUsageStats: GetDailyUsageStats,
    private val backgroundDispatcher: CoroutineDispatcher
) : GetWeeklyUsageStats {

    private val daysOfWeek = Week.values()

    override suspend fun invoke(weekOffset: Int): List<UsageStats> =
        withContext(backgroundDispatcher) {
            val usageStatsList = mutableListOf<UsageStats>()
            daysOfWeek.forEach { dayOfWeek ->
                val usageStats = dailyUsageStats(weekOffset, dayOfWeek.isoDayNumber)
                usageStatsList.add(usageStats)
            }
            usageStatsList.toList()
        }
}