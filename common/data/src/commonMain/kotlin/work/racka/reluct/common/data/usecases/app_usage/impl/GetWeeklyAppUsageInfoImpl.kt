package work.racka.reluct.common.data.usecases.app_usage.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import work.racka.reluct.common.data.usecases.app_usage.GetDailyAppUsageInfo
import work.racka.reluct.common.data.usecases.app_usage.GetWeeklyAppUsageInfo
import work.racka.reluct.common.model.domain.usagestats.AppUsageStats
import work.racka.reluct.common.model.util.time.Week

internal class GetWeeklyAppUsageInfoImpl(
    private val dailyAppUsageInfo: GetDailyAppUsageInfo,
    private val backgroundDispatcher: CoroutineDispatcher
) : GetWeeklyAppUsageInfo {

    private val daysOfWeek = Week.values()

    override suspend fun invoke(weekOffset: Int, packageName: String): List<AppUsageStats> =
        withContext(backgroundDispatcher) {
            val appUsageList = mutableListOf<AppUsageStats>()
            daysOfWeek.forEach { dayOfWeek ->
                val appStats = dailyAppUsageInfo(
                    weekOffset = weekOffset,
                    dayIsoNumber = dayOfWeek.isoDayNumber,
                    packageName = packageName
                )
                appUsageList.add(appStats)
            }
            appUsageList
        }
}