package work.racka.reluct.common.data.usecases.app_usage.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import work.racka.reluct.common.app.usage.stats.manager.UsageDataManager
import work.racka.reluct.common.data.mappers.usagestats.asAppUsageStats
import work.racka.reluct.common.data.usecases.app_usage.GetDailyAppUsageInfo
import work.racka.reluct.common.model.domain.usagestats.AppUsageStats
import work.racka.reluct.common.model.util.time.StatisticsTimeUtils

internal class GetDailyAppUsageInfoImpl(
    private val usageManager: UsageDataManager,
    private val backgroundDispatcher: CoroutineDispatcher
) : GetDailyAppUsageInfo {
    override suspend fun invoke(
        weekOffset: Int,
        dayIsoNumber: Int,
        packageName: String
    ): AppUsageStats =
        withContext(backgroundDispatcher) {
            val selectedDayTimeRange = StatisticsTimeUtils.selectedDayTimeInMillisRange(
                weekOffset = weekOffset,
                dayIsoNumber = dayIsoNumber
            )
            val appStats = usageManager.getAppUsage(
                startTimeMillis = selectedDayTimeRange.first,
                endTimeMillis = selectedDayTimeRange.last,
                packageName = packageName
            )
            appStats.asAppUsageStats(
                weekOffset = weekOffset,
                dayIsoNumber = dayIsoNumber
            )
        }
}