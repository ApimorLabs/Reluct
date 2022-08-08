package work.racka.reluct.common.model.domain.usagestats

import work.racka.reluct.common.model.domain.core.Icon

data class AppUsageInfo(
    val packageName: String,
    val appName: String,
    val appIcon: Icon,
    val timeInForeground: Long = 0,
    val formattedTimeInForeground: String = "",
    val appLaunchCount: Int = 0,
)