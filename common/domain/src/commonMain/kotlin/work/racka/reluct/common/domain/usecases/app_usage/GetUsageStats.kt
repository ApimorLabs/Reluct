package work.racka.reluct.common.domain.usecases.app_usage

import work.racka.reluct.common.model.domain.usagestats.UsageStats
import work.racka.reluct.common.model.util.time.Week

interface GetUsageStats {
    suspend fun dailyUsage(weekOffset: Int = 0, dayIsoNumber: Int): UsageStats
    suspend fun weeklyUsage(weekOffset: Int = 0): Map<Week, UsageStats>
}