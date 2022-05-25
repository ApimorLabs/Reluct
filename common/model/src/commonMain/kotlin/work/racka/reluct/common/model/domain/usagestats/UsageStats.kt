package work.racka.reluct.common.model.domain.usagestats

import work.racka.reluct.common.model.util.time.Week

data class UsageStats(
    val appsUsageList: List<AppUsageInfo>,
    val dayOfWeek: Week,
    val totalScreenTime: Long,
    val unlockCount: Long,
)