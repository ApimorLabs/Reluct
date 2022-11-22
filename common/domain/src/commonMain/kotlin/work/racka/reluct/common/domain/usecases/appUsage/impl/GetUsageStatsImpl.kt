package work.racka.reluct.common.domain.usecases.appUsage.impl

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import work.racka.reluct.common.app.usage.stats.manager.UsageDataManager
import work.racka.reluct.common.domain.mappers.usagestats.asUsageStats
import work.racka.reluct.common.domain.usecases.appInfo.GetAppInfo
import work.racka.reluct.common.domain.usecases.appUsage.GetUsageStats
import work.racka.reluct.common.model.domain.usagestats.UsageStats
import work.racka.reluct.common.model.util.time.StatisticsTimeUtils
import work.racka.reluct.common.model.util.time.Week

internal class GetUsageStatsImpl(
    private val usageManager: UsageDataManager,
    private val getAppInfo: GetAppInfo,
    private val backgroundDispatcher: CoroutineDispatcher
) : GetUsageStats {
    private val daysOfWeek = Week.values()

    override suspend fun dailyUsage(weekOffset: Int, dayIsoNumber: Int): UsageStats =
        withContext(backgroundDispatcher) {
            val selectedDayTimeRange = StatisticsTimeUtils.selectedDayTimeInMillisRange(
                weekOffset = weekOffset,
                dayIsoNumber = dayIsoNumber
            )
            val dataUsageStats = usageManager.getUsageStats(
                startTimeMillis = selectedDayTimeRange.first,
                endTimeMillis = selectedDayTimeRange.last
            )
            dataUsageStats.asUsageStats(
                weekOffset = weekOffset,
                dayIsoNumber = dayIsoNumber,
                getAppInfo = getAppInfo
            )
        }

    override suspend fun weeklyUsage(weekOffset: Int): ImmutableMap<Week, UsageStats> =
        withContext(backgroundDispatcher) {
            persistentMapOf<Week, UsageStats>().builder().apply {
                for (day in daysOfWeek) {
                    put(
                        key = day,
                        value = dailyUsage(weekOffset, day.isoDayNumber)
                    )
                }
            }.build().toImmutableMap()
        }
}
