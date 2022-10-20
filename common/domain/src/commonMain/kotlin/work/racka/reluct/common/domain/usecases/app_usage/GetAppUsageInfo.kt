package work.racka.reluct.common.domain.usecases.app_usage

import work.racka.reluct.common.model.domain.usagestats.AppUsageStats
import work.racka.reluct.common.model.util.time.Week

interface GetAppUsageInfo {
    suspend fun dailUsage(
        weekOffset: Int = 0,
        dayIsoNumber: Int,
        packageName: String
    ): AppUsageStats

    suspend fun weeklyUsage(weekOffset: Int, packageName: String): Map<Week, AppUsageStats>
}