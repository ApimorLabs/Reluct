package work.racka.reluct.common.data.usecases.app_usage.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import work.racka.reluct.common.app.usage.stats.manager.UsageDataManager
import work.racka.reluct.common.data.mappers.usagestats.asAppUsageStats
import work.racka.reluct.common.data.usecases.app_usage.GetWeeklyAppUsageInfoUseCase
import work.racka.reluct.common.model.domain.usagestats.AppUsageStats
import work.racka.reluct.common.model.util.time.StatisticsTimeUtils
import work.racka.reluct.common.model.util.time.Week

internal class GetWeeklyAppUsageInfoUseCaseImpl(
    private val usageManager: UsageDataManager,
    private val backgroundDispatcher: CoroutineDispatcher
) : GetWeeklyAppUsageInfoUseCase {

    private val daysOfWeek = Week.values()

    private fun getWeekTimeInMillisRange(weekOffset: Int): Array<Pair<Week, LongRange>> =
        daysOfWeek.map { dayOfWeek ->
            val range = StatisticsTimeUtils.selectedDayTimeInMillisRange(
                weekOffset = weekOffset,
                dayIsoNumber = dayOfWeek.isoDayNumber
            )
            Pair(dayOfWeek, range)
        }.toTypedArray()

    override suspend fun invoke(weekOffset: Int, packageName: String): List<AppUsageStats> =
        withContext(backgroundDispatcher) {
            val appUsageList = mutableListOf<AppUsageStats>()
            getWeekTimeInMillisRange(weekOffset).forEach { rangePair ->
                val appStats = usageManager.getAppUsage(
                    startTimeMillis = rangePair.second.first,
                    endTimeMillis = rangePair.second.last,
                    packageName = packageName
                )
                appUsageList.add(
                    appStats.asAppUsageStats(
                        weekOffset = weekOffset,
                        dayIsoNumber = rangePair.first.isoDayNumber
                    )
                )
            }
            appUsageList
        }
}