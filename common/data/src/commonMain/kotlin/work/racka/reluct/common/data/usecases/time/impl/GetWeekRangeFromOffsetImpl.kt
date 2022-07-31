package work.racka.reluct.common.data.usecases.time.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import work.racka.reluct.common.data.usecases.time.GetWeekRangeFromOffset
import work.racka.reluct.common.model.util.time.StatisticsTimeUtils
import work.racka.reluct.common.model.util.time.TimeUtils

internal class GetWeekRangeFromOffsetImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GetWeekRangeFromOffset {
    override suspend fun invoke(weekOffset: Int): String = withContext(dispatcher) {
        // Monday to Sunday
        val weeklyTimeRange = StatisticsTimeUtils
            .weekLocalDateTimeStringRange(weekOffset = weekOffset)

        if (weekOffset == 0) "This Week"
        else {
            val start = TimeUtils.getFormattedDateString(
                dateTime = weeklyTimeRange.start,
                showShortIntervalAsDay = false
            )
            val end = TimeUtils.getFormattedDateString(
                dateTime = weeklyTimeRange.endInclusive,
                showShortIntervalAsDay = false
            )
            "$start - $end"
        }
    }
}