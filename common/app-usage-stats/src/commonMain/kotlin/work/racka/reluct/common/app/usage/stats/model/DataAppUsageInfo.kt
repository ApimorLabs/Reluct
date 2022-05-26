package work.racka.reluct.common.app.usage.stats.model

import work.racka.reluct.common.model.domain.usagestats.Icon

data class DataAppUsageInfo(
    val packageName: String,
    val appName: String,
    val appIcon: Icon,
    var timeInForeground: Long = 0,
    var appLaunchCount: Int = 0,
)
