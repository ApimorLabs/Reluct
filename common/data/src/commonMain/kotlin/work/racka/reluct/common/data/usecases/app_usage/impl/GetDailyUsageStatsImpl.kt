package work.racka.reluct.common.data.usecases.app_usage.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import work.racka.reluct.common.app.usage.stats.manager.UsageDataManager
import work.racka.reluct.common.data.mappers.usagestats.asUsageStats
import work.racka.reluct.common.data.usecases.app_info.GetAppInfo
import work.racka.reluct.common.data.usecases.app_usage.GetDailyUsageStats
import work.racka.reluct.common.model.domain.usagestats.UsageStats
import work.racka.reluct.common.model.util.time.StatisticsTimeUtils

internal class GetDailyUsageStatsImpl(
    private val usageManager: UsageDataManager,
    private val getAppInfo: GetAppInfo,
    private val backgroundDispatcher: CoroutineDispatcher
) : GetDailyUsageStats {

    override suspend fun invoke(weekOffset: Int, dayIsoNumber: Int): UsageStats =
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
}