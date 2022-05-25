package work.racka.reluct.common.data.usecases.app_usage

import work.racka.reluct.common.model.domain.usagestats.UsageStats

interface GetWeeklyUsageStatsUseCase {
    operator fun invoke(weekOffset: Int = 0): List<UsageStats>
}