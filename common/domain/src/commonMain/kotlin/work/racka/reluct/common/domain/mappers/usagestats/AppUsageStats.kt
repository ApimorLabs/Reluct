package work.racka.reluct.common.domain.mappers.usagestats

import work.racka.reluct.common.app.usage.stats.model.DataAppUsageInfo
import work.racka.reluct.common.domain.usecases.app_info.GetAppInfo
import work.racka.reluct.common.model.domain.usagestats.AppUsageStats
import work.racka.reluct.common.model.util.time.StatisticsTimeUtils
import work.racka.reluct.common.model.util.time.TimeUtils

suspend fun DataAppUsageInfo.asAppUsageStats(
    weekOffset: Int,
    dayIsoNumber: Int,
    showIntervalAsDay: Boolean = true,
    getAppInfo: GetAppInfo
): AppUsageStats {
    val selectedDayDateTimeString = StatisticsTimeUtils.selectedDayDateTimeString(
        weekOffset = weekOffset,
        selectedDayIsoNumber = dayIsoNumber
    )
    return AppUsageStats(
        appUsageInfo = this.asAppUsageInfo(getAppInfo),
        dateFormatted = TimeUtils.getFormattedDateString(
            dateTime = selectedDayDateTimeString,
            showShortIntervalAsDay = showIntervalAsDay
        )
    )
}