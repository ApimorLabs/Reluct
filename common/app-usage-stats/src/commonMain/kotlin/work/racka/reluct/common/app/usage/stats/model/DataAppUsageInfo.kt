package work.racka.reluct.common.app.usage.stats.model

data class DataAppUsageInfo(
    val packageName: String,
    var timeInForeground: Long = 0,
    var appLaunchCount: Int = 0,
)
