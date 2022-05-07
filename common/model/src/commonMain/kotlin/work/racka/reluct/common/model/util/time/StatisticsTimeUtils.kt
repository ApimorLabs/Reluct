package work.racka.reluct.common.model.util.time

import kotlinx.datetime.*
import work.racka.reluct.common.model.util.time.TimeUtils.plus

object StatisticsTimeUtils {

    private fun startOfWeekLocalDateTimeString(
        weekOffset: Int = 0,
        timeZoneId: String = TimeZone.currentSystemDefault().id,
    ): LocalDateTime {
        Clock.System.now().toLocalDateTime(TimeZone.of(timeZoneId)).apply {
            val new = plus(days = weekOffset * 7, timeZoneId = timeZoneId)
                .plus(days = 1 - dayOfWeek.isoDayNumber, timeZoneId = timeZoneId)
            return LocalDateTime(
                new.year,
                new.month,
                new.dayOfMonth,
                0,
                0,
                0,
                0
            )
        }
    }

    private fun endOfWeekLocalDateTimeString(
        weekOffset: Int = 0,
        timeZoneId: String = TimeZone.currentSystemDefault().id,
    ): LocalDateTime {
        Clock.System.now().toLocalDateTime(TimeZone.of(timeZoneId)).apply {
            val new = plus(days = weekOffset * 7, timeZoneId = timeZoneId)
                .plus(days = 7 - dayOfWeek.isoDayNumber, timeZoneId = timeZoneId)
            return LocalDateTime(
                new.year,
                new.month,
                new.dayOfMonth,
                23,
                59,
                59,
                999_999_999
            )
        }
    }

    private fun selectedDayDateTime(
        weekOffset: Int = 0,
        selectedDayIsoNumber: Int = 1,
        timeZoneId: String = TimeZone.currentSystemDefault().id,
    ): LocalDateTime {
        val localDateTime = Clock.System.now().toLocalDateTime(TimeZone.of(timeZoneId))
        return localDateTime.plus(days = weekOffset * 7, timeZoneId = timeZoneId)
            .plus(days = selectedDayIsoNumber - localDateTime.dayOfWeek.isoDayNumber,
                timeZoneId = timeZoneId)
    }

    fun weekLocalDateTimeStringRange(
        weekOffset: Int = 0,
        timeZoneId: String = TimeZone.currentSystemDefault().id,
    ): ClosedRange<String> {
        val start = startOfWeekLocalDateTimeString(weekOffset, timeZoneId).toString()
        val end = endOfWeekLocalDateTimeString(weekOffset, timeZoneId).toString()
        return start..end
    }

    fun selectedDayDateTimeStringRange(
        weekOffset: Int = 0,
        dayIsoNumber: Int = 1,
        timeZoneId: String = TimeZone.currentSystemDefault().id,
    ): ClosedRange<String> {
        val selectedDay = selectedDayDateTime(weekOffset, dayIsoNumber, timeZoneId)
        val start =
            LocalDateTime(selectedDay.year, selectedDay.month, selectedDay.dayOfMonth, 0, 0, 0, 0)
                .toString()
        val end = LocalDateTime(selectedDay.year,
            selectedDay.month,
            selectedDay.dayOfMonth,
            23,
            59,
            59,
            999_999_999)
            .toString()
        return start..end
    }
}