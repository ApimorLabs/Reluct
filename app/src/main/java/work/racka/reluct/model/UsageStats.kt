package work.racka.reluct.model

import work.racka.reluct.data.local.usagestats.AppUsageInfo
import work.racka.reluct.data.local.usagestats.Week

data class UsageStats(
    val appsUsageList: List<AppUsageInfo>,
    val dayOfWeek: Week,
    val date: String,
    val totalScreenTime: Long,
    val unlockCount: Long
)
