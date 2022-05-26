package work.racka.reluct.common.model.domain.usagestats

data class AppUsageStats(
    val appUsageInfo: AppUsageInfo,
    val dateFormatted: String = "...",
    val dayIsoNumber: Int = 0
)