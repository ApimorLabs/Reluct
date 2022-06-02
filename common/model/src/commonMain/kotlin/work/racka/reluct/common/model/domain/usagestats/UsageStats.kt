package work.racka.reluct.common.model.domain.usagestats

data class UsageStats(
    val appsUsageList: List<AppUsageInfo> = emptyList(),
    val dateFormatted: String = "...",
    val dayIsoNumber: Int = 0,
    val totalScreenTime: Long = 0L,
    val unlockCount: Long = 0L
)