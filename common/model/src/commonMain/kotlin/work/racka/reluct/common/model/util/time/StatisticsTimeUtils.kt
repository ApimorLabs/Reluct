package work.racka.reluct.common.model.util.time

import kotlinx.datetime.*
import work.racka.reluct.common.model.util.time.TimeConstants.DAILY_HOURS
import work.racka.reluct.common.model.util.time.TimeConstants.DAYS_OF_WEEK
import work.racka.reluct.common.model.util.time.TimeConstants.HOURLY_MINUTES_SECONDS
import work.racka.reluct.common.model.util.time.TimeConstants.MINUTE_MILLIS
import work.racka.reluct.common.model.util.time.TimeUtils.plus

object StatisticsTimeUtils {

    private fun startOfWeekLocalDateTime(
        weekOffset: Int = 0,
        timeZoneId: String = TimeZone.currentSystemDefault().id,
    ): LocalDateTime {
        Clock.System.now().toLocalDateTime(TimeZone.of(timeZoneId)).apply {
            val new = plus(days = weekOffset * DAYS_OF_WEEK, timeZoneId = timeZoneId)
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

    private fun endOfWeekLocalDateTime(
        weekOffset: Int = 0,
        timeZoneId: String = TimeZone.currentSystemDefault().id,
    ): LocalDateTime {
        Clock.System.now().toLocalDateTime(TimeZone.of(timeZoneId)).apply {
            val new = plus(days = weekOffset * DAYS_OF_WEEK, timeZoneId = timeZoneId)
                .plus(days = DAYS_OF_WEEK - dayOfWeek.isoDayNumber, timeZoneId = timeZoneId)
            return LocalDateTime(
                new.year,
                new.month,
                new.dayOfMonth,
                DAILY_HOURS - 1,
                HOURLY_MINUTES_SECONDS - 1,
                HOURLY_MINUTES_SECONDS - 1,
                MINUTE_MILLIS - 1
            )
        }
    }

    private fun selectedDayDateTime(
        weekOffset: Int = 0,
        selectedDayIsoNumber: Int = 1,
        timeZoneId: String = TimeZone.currentSystemDefault().id,
    ): LocalDateTime {
        val localDateTime = Clock.System.now().toLocalDateTime(TimeZone.of(timeZoneId))
        return localDateTime.plus(days = weekOffset * DAYS_OF_WEEK, timeZoneId = timeZoneId)
            .plus(
                days = selectedDayIsoNumber - localDateTime.dayOfWeek.isoDayNumber,
                timeZoneId = timeZoneId
            )
    }

    fun weekLocalDateTimeStringRange(
        weekOffset: Int = 0,
        timeZoneId: String = TimeZone.currentSystemDefault().id,
    ): ClosedRange<String> {
        val start = startOfWeekLocalDateTime(weekOffset, timeZoneId).toString()
        val end = endOfWeekLocalDateTime(weekOffset, timeZoneId).toString()
        return start..end
    }

    fun weekTimeInMillisRange(
        weekOffset: Int = 0,
        timeZoneId: String = TimeZone.currentSystemDefault().id,
    ): LongRange {
        val timeZone = TimeZone.of(timeZoneId)
        val start = startOfWeekLocalDateTime(weekOffset, timeZoneId)
            .toInstant(timeZone).toEpochMilliseconds()
        val end = endOfWeekLocalDateTime(weekOffset, timeZoneId)
            .toInstant(timeZone).toEpochMilliseconds()
        return start..end
    }

    fun selectedDayDateTimeString(
        weekOffset: Int = 0,
        selectedDayIsoNumber: Int = 1,
        timeZoneId: String = TimeZone.currentSystemDefault().id,
    ): String = selectedDayDateTime(weekOffset, selectedDayIsoNumber, timeZoneId).toString()

    fun selectedDayTimeInMillisRange(
        weekOffset: Int = 0,
        dayIsoNumber: Int = 1,
        timeZoneId: String = TimeZone.currentSystemDefault().id,
    ): LongRange {
        val timeZone = TimeZone.of(timeZoneId)
        val selectedDay = selectedDayDateTime(weekOffset, dayIsoNumber, timeZoneId)
        val start =
            LocalDateTime(selectedDay.year, selectedDay.month, selectedDay.dayOfMonth, 0, 0, 0, 0)
                .toInstant(timeZone).toEpochMilliseconds()
        val end = LocalDateTime(
            selectedDay.year,
            selectedDay.month,
            selectedDay.dayOfMonth,
            DAILY_HOURS - 1,
            HOURLY_MINUTES_SECONDS - 1,
            HOURLY_MINUTES_SECONDS - 1,
            MINUTE_MILLIS - 1
        )
            .toInstant(timeZone).toEpochMilliseconds()
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
        val end = LocalDateTime(
            selectedDay.year,
            selectedDay.month,
            selectedDay.dayOfMonth,
            DAILY_HOURS - 1,
            HOURLY_MINUTES_SECONDS - 1,
            HOURLY_MINUTES_SECONDS - 1,
            MINUTE_MILLIS - 1
        )
            .toString()
        return start..end
    }
}

internal object TimeConstants {
    const val DAYS_OF_WEEK = 7
    const val DAILY_HOURS = 24
    const val HOURLY_MINUTES_SECONDS = 60
    const val MINUTE_MILLIS = 1_000_000_000
    const val MILLIS_PER_HOUR = 3.6e6
    const val MILLIS_PER_MINUTE = 60000
    const val MILLIS_PER_SECOND = 1000
}
