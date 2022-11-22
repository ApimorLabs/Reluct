package work.racka.reluct.common.domain.usecases.appUsage.impl

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import work.racka.reluct.common.app.usage.stats.manager.UsageDataManager
import work.racka.reluct.common.domain.mappers.usagestats.asAppUsageStats
import work.racka.reluct.common.domain.usecases.appInfo.GetAppInfo
import work.racka.reluct.common.domain.usecases.appUsage.GetAppUsageInfo
import work.racka.reluct.common.model.domain.usagestats.AppUsageStats
import work.racka.reluct.common.model.util.time.StatisticsTimeUtils
import work.racka.reluct.common.model.util.time.Week

internal class GetAppUsageInfoImpl(
    private val usageManager: UsageDataManager,
    private val backgroundDispatcher: CoroutineDispatcher,
    private val getAppInfo: GetAppInfo
) : GetAppUsageInfo {

    private val daysOfWeek = Week.values()

    override suspend fun dailUsage(
        weekOffset: Int,
        dayIsoNumber: Int,
        packageName: String
    ): AppUsageStats = withContext(backgroundDispatcher) {
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
            dayIsoNumber = dayIsoNumber,
            getAppInfo = getAppInfo
        )
    }

    override suspend fun weeklyUsage(
        weekOffset: Int,
        packageName: String
    ): ImmutableMap<Week, AppUsageStats> = withContext(backgroundDispatcher) {
        persistentMapOf<Week, AppUsageStats>().builder().apply {
            for (week in daysOfWeek) {
                put(
                    key = week,
                    value = dailUsage(
                        weekOffset = weekOffset,
                        dayIsoNumber = week.isoDayNumber,
                        packageName = packageName
                    )
                )
            }
        }.build().toImmutableMap()
    }
}
