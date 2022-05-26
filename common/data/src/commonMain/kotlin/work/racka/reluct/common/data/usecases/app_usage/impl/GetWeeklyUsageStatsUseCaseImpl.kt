package work.racka.reluct.common.data.usecases.app_usage.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import work.racka.reluct.common.data.usecases.app_usage.GetDailyUsageStatsUseCase
import work.racka.reluct.common.data.usecases.app_usage.GetWeeklyUsageStatsUseCase
import work.racka.reluct.common.model.domain.usagestats.UsageStats
import work.racka.reluct.common.model.util.time.Week

internal class GetWeeklyUsageStatsUseCaseImpl(
    private val dailyUsageStats: GetDailyUsageStatsUseCase,
    private val backgroundDispatcher: CoroutineDispatcher
) : GetWeeklyUsageStatsUseCase {

    private val daysOfWeek = Week.values()

    override suspend fun invoke(weekOffset: Int): List<UsageStats> =
        withContext(backgroundDispatcher) {
            val usageStatsList = mutableListOf<UsageStats>()
            daysOfWeek.forEach { dayOfWeek ->
                val usageStats = dailyUsageStats(weekOffset, dayOfWeek.isoDayNumber)
            }
            usageStatsList.toList()
        }
}