package work.racka.reluct.common.model.domain.usagestats

data class AppUsageInfo(
    val packageName: String,
    val appIcon: Icon?,
    val dominantColor: Int,
    val timeInForeground: Long = 0,
    val appLaunchCount: Int = 0
)