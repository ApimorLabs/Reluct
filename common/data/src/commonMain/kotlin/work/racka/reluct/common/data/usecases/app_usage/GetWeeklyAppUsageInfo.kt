package work.racka.reluct.common.data.usecases.app_usage

import work.racka.reluct.common.model.domain.usagestats.AppUsageStats
import work.racka.reluct.common.model.util.time.Week

interface GetWeeklyAppUsageInfo {
    suspend fun invoke(weekOffset: Int, packageName: String): Map<Week, AppUsageStats>
}