package work.racka.reluct.common.data.mappers.usagestats

import work.racka.reluct.common.app.usage.stats.model.DataAppUsageInfo
import work.racka.reluct.common.data.usecases.app_info.GetAppInfo
import work.racka.reluct.common.model.domain.usagestats.AppUsageInfo
import work.racka.reluct.common.model.util.time.TimeUtils

suspend fun DataAppUsageInfo.asAppUsageInfo(getAppInfo: GetAppInfo): AppUsageInfo = AppUsageInfo(
    packageName = this.packageName,
    appName = getAppInfo.getAppName(packageName),
    appIcon = getAppInfo.getAppIcon(packageName),
    timeInForeground = this.timeInForeground,
    formattedTimeInForeground = TimeUtils
        .getFormattedTimeDurationString(this.timeInForeground),
    appLaunchCount = this.appLaunchCount
)