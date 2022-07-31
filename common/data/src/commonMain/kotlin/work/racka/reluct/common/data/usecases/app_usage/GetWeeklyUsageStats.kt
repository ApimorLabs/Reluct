package work.racka.reluct.common.data.usecases.app_usage

import work.racka.reluct.common.model.domain.usagestats.UsageStats
import work.racka.reluct.common.model.util.time.Week

interface GetWeeklyUsageStats {
    suspend fun invoke(weekOffset: Int = 0): Map<Week, UsageStats>
}