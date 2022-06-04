package work.racka.reluct.common.data.mappers.usagestats

import work.racka.reluct.common.app.usage.stats.model.DataAppUsageInfo
import work.racka.reluct.common.model.domain.usagestats.AppUsageInfo
import work.racka.reluct.common.model.util.time.TimeUtils

fun DataAppUsageInfo.asAppUsageInfo(): AppUsageInfo = AppUsageInfo(
    packageName = this.packageName,
    appName = this.appName,
    appIcon = this.appIcon,
    timeInForeground = this.timeInForeground,
    formattedTimeInForeground = TimeUtils
        .getFormattedTimeDurationString(this.timeInForeground),
    appLaunchCount = this.appLaunchCount
)