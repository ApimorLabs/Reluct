package work.racka.reluct.common.model.domain.usagestats

data class UsageStats(
    val appsUsageList: List<AppUsageInfo> = emptyList(),
    val dateFormatted: String = "...",
    val totalScreenTime: Long = 0L,
    val formattedTotalScreenTime: String = "",
    val unlockCount: Long = 0L
)