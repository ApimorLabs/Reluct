package work.racka.reluct.common.data.mappers.usagestats

import work.racka.reluct.common.app.usage.stats.model.DataAppUsageInfo
import work.racka.reluct.common.model.domain.usagestats.AppUsageStats
import work.racka.reluct.common.model.util.time.StatisticsTimeUtils
import work.racka.reluct.common.model.util.time.TimeUtils

fun DataAppUsageInfo.asAppUsageStats(
    weekOffset: Int,
    dayIsoNumber: Int,
    showIntervalAsDay: Boolean = true
): AppUsageStats {
    val selectedDayDateTimeString = StatisticsTimeUtils.selectedDayDateTimeString(
        weekOffset = weekOffset,
        selectedDayIsoNumber = dayIsoNumber
    )
    return AppUsageStats(
        appUsageInfo = this.asAppUsageInfo(),
        dateFormatted = TimeUtils.getFormattedDateString(
            dateTime = selectedDayDateTimeString,
            showShortIntervalAsDay = showIntervalAsDay
        )
    )
}