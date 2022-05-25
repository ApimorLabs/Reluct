package work.racka.reluct.common.model.domain.usagestats

data class AppUsageInfo(
    val packageName: String,
    val appName: String,
    val appIcon: Icon?,
    val timeInForeground: Long = 0,
    val appLaunchCount: Int = 0,
)