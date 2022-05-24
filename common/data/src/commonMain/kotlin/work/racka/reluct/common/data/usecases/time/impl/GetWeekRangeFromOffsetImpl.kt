package work.racka.reluct.common.data.usecases.time.impl

import work.racka.reluct.common.data.usecases.time.GetWeekRangeFromOffset
import work.racka.reluct.common.model.util.time.StatisticsTimeUtils
import work.racka.reluct.common.model.util.time.TimeUtils

class GetWeekRangeFromOffsetImpl : GetWeekRangeFromOffset {
    override fun invoke(weekOffset: Int): String {
        // Monday to Sunday
        val weeklyTimeRange = StatisticsTimeUtils
            .weekLocalDateTimeStringRange(weekOffset = weekOffset)

        return if (weekOffset == 0) "This Week"
        else {
            val start = TimeUtils.getFormattedDateString(
                dateTime = weeklyTimeRange.start,
                showShortIntervalAsDay = false)
            val end = TimeUtils.getFormattedDateString(
                dateTime = weeklyTimeRange.endInclusive,
                showShortIntervalAsDay = false)
            "$start - $end"
        }
    }
}