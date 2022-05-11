package work.racka.reluct.common.model.util.time

import kotlinx.datetime.*
import kotlin.math.abs

object TimeUtils {

    /**
     * Current LocalDateTime as string
     */
    fun currentLocalDateTime(timeZoneId: String = TimeZone.currentSystemDefault().id): String {
        val timeZone = TimeZone.of(timeZoneId)
        return Clock.System.now().toLocalDateTime(timeZone).toString()
    }

    /**
     * Retrieve the correct LocalDateTime with account to TimeZone
     */
    fun getLocalDateTimeWithCorrectTimeZone(
        dateTime: String,
        originalTimeZoneId: String,
    ): LocalDateTime {
        val timeZone = TimeZone.of(originalTimeZoneId)
        val localDateTime = dateTime.toLocalDateTime()
        val instant = localDateTime.toInstant(timeZone)

        /**
         * We convert the instant back to LocalDateTime after applying the TimeZone
         * We get this TimeZone from the user's device in case the user has moved
         * to a new TimeZone from the one stored when item was stored (i.e a Task)
         */
        return instant.toLocalDateTime(TimeZone.currentSystemDefault())
    }

    /**
     * Check if time provided is already overdue
     * Returns a Boolean
     */
    fun isDateTimeOverdue(
        dateTime: String,
        originalTimeZoneId: String,
        overdueHours: Int = 1,
    ): Boolean {
        val today = Clock.System.now()
        val localDTInstant = getLocalDateTimeWithCorrectTimeZone(dateTime, originalTimeZoneId)
            .toInstant(TimeZone.currentSystemDefault())
        val diff = today - localDTInstant
        return diff.inWholeHours >= overdueHours
    }

    /**
     * Get a formatted day or date for grouping
     * If showShortIntervalAsDay is true then it will show Today, Yesterday, Tomorrow
     * or a day instead of the full date when the interval is within a 3 days difference.
     * If false it will show the full date even if the interval is within a 3 days difference
     */
    fun getFormattedDateString(
        dateTime: String,
        originalTimeZoneId: String = TimeZone.currentSystemDefault().id,
        showShortIntervalAsDay: Boolean = true,
    ): String {
        val today = Clock.System.todayAt(TimeZone.currentSystemDefault())
        val localDT = getLocalDateTimeWithCorrectTimeZone(dateTime, originalTimeZoneId)

        // If we don't want to show Day of week itself when intervals are near
        // we just return from here and skip the rest of the code
        if (!showShortIntervalAsDay) {
            return if (today.year == localDT.year) {
                "${localDT.dayOfWeek.toDayOfWeekShortenedString()}, " +
                        "${localDT.month.toMonthShortenedString()} ${localDT.dayOfMonth}"
            } else {
                "${localDT.month.toMonthShortenedString()} ${localDT.dayOfMonth}, ${localDT.year}"
            }
        }

        val date = LocalDate(
            localDT.year,
            localDT.monthNumber,
            localDT.dayOfMonth
        )
        val daysUntil = today.daysUntil(date)
        return when {
            daysUntil == -2 -> date.dayOfWeek.toDayOfWeekString()
            daysUntil == -1 -> "Yesterday"
            daysUntil == 0 -> "Today"
            daysUntil == 1 -> "Tomorrow"
            daysUntil == 2 -> date.dayOfWeek.toDayOfWeekString()
            today.year != date.year -> {
                "${date.month.toMonthShortenedString()} ${date.dayOfMonth}, ${date.year}"
            }
            else -> {
                "${date.dayOfWeek.toDayOfWeekShortenedString()}, " +
                        "${date.month.toMonthShortenedString()} ${date.dayOfMonth}"
            }
        }
    }

    /**
     * This accounts for changes in Timezone. So you need the TimeZoneId
     * The TimeZoneId needed is the one from the database. This will convert
     * the LocalDateTime stored to the correct system TimeZone if it's different
     * It will return a Time string in the format of HH:MM
     */
    fun getTimeFromLocalDateTime(
        dateTime: String,
        originalTimeZoneId: String = TimeZone.currentSystemDefault().id,
    ): String {
        val localDateTime = getLocalDateTimeWithCorrectTimeZone(dateTime, originalTimeZoneId)
        val hr = localDateTime.hour
        val min =
            if (localDateTime.minute < 10) "0${localDateTime.minute}" else localDateTime.minute
        return "$hr:$min"
    }

    /**
     * Consolidates both date and time to a human readable format.
     * This accounts for changes in Timezone. So you need the TimeZoneId
     */
    fun getDateAndTime(
        localDateTime: String,
        showShortIntervalAsDay: Boolean = true,
        timeZoneId: String = TimeZone.currentSystemDefault().id,
        separator: String = "-",
    ): String {
        val date = getFormattedDateString(
            dateTime = localDateTime,
            originalTimeZoneId = timeZoneId,
            showShortIntervalAsDay = showShortIntervalAsDay
        )
        val time = getTimeFromLocalDateTime(
            dateTime = localDateTime,
            originalTimeZoneId = timeZoneId
        )
        return "$date $separator $time"
    }

    /**
     * This accounts for changes in Timezone. So you need the TimeZoneId
     * The TimeZoneId needed is the one from the database. This will convert
     * the LocalDateTime stored to the correct system TimeZone if it's different
     * It will return a countdown or time passed since the provided LocalDateTime
     */
    fun getTimeLeftFromLocalDateTime(
        dateTime: String,
        originalTimeZoneId: String = TimeZone.currentSystemDefault().id,
    ): String {
        val currentTime = Clock.System.now()
        val instant = getLocalDateTimeWithCorrectTimeZone(dateTime, originalTimeZoneId)
            .toInstant(TimeZone.currentSystemDefault())
        val diff = currentTime.periodUntil(instant, TimeZone.currentSystemDefault())
        val data = when {
            diff.years > 0 -> {
                val yr = timePluralOrSingle(diff.years, TimePeriod.YEAR)
                val mth = timePluralOrSingle(diff.months, TimePeriod.MONTH)
                "In $yr $mth"
            }
            diff.months > 0 -> {
                val mth = timePluralOrSingle(diff.months, TimePeriod.MONTH)
                "In $mth"
            }
            diff.days > 0 -> {
                val days = timePluralOrSingle(diff.days, TimePeriod.DAY)
                val hrs = timePluralOrSingle(diff.hours, TimePeriod.HOUR)
                "In $days $hrs"
            }
            diff.hours > 0 -> {
                val hrs = timePluralOrSingle(diff.hours, TimePeriod.HOUR)
                val min = timePluralOrSingle(diff.minutes, TimePeriod.MINUTE)
                "In $hrs $min"
            }
            diff.minutes > 0 -> {
                val min = timePluralOrSingle(diff.minutes, TimePeriod.MINUTE)
                "In $min"
            }
            diff.years < 0 -> {
                val yr = timePluralOrSingle(diff.years, TimePeriod.YEAR)
                val mth = timePluralOrSingle(diff.months, TimePeriod.MONTH)
                "$yr $mth ago"
            }
            diff.months < 0 -> {
                val mth = timePluralOrSingle(diff.months, TimePeriod.MONTH)
                "$mth ago"
            }
            diff.days < 0 -> {
                val days = timePluralOrSingle(diff.days, TimePeriod.DAY)
                val hrs = timePluralOrSingle(diff.hours, TimePeriod.HOUR)
                "$days $hrs ago"
            }
            diff.hours <= 1 -> {
                val hrs = timePluralOrSingle(diff.hours, TimePeriod.HOUR)
                val min = timePluralOrSingle(diff.minutes, TimePeriod.MINUTE)
                "$hrs $min ago"
            }
            diff.minutes == 0 -> "Ongoing ${diff.days}"
            else -> "Error ${diff.hours}"
        }
        return data
    }

    /**
     * Return provided Period of time with a plural or single abbreviation appended
     * to it
     */
    private fun timePluralOrSingle(value: Int, period: TimePeriod): String {
        val abs = abs(value)
        return when (period) {
            TimePeriod.MINUTE -> {
                when (abs) {
                    1 -> "$abs minute"
                    0 -> ""
                    else -> "$abs minutes"
                }
            }
            TimePeriod.HOUR -> {
                when (abs) {
                    1 -> "$abs hour"
                    0 -> ""
                    else -> "$abs hours"
                }
            }
            TimePeriod.DAY -> {
                when (abs) {
                    1 -> "$abs day"
                    0 -> ""
                    else -> "$abs days"
                }
            }
            TimePeriod.MONTH -> {
                when (abs) {
                    1 -> "$abs month"
                    0 -> ""
                    else -> "$abs months"
                }
            }
            TimePeriod.YEAR -> {
                when (abs) {
                    1 -> "$abs year"
                    0 -> ""
                    else -> "$abs years"
                }
            }
        }
    }

    /**
     * Get a string of the day of week provided
     */
    fun DayOfWeek.toDayOfWeekShortenedString(): String =
        when (this) {
            DayOfWeek.MONDAY -> "Mon"
            DayOfWeek.TUESDAY -> "Tue"
            DayOfWeek.WEDNESDAY -> "Wed"
            DayOfWeek.THURSDAY -> "Thu"
            DayOfWeek.FRIDAY -> "Fri"
            DayOfWeek.SATURDAY -> "Sat"
            DayOfWeek.SUNDAY -> "Sun"
            else -> throw IllegalArgumentException()
        }

    private fun DayOfWeek.toDayOfWeekString(): String =
        when (this) {
            DayOfWeek.MONDAY -> "Monday"
            DayOfWeek.TUESDAY -> "Tuesday"
            DayOfWeek.WEDNESDAY -> "Wednesday"
            DayOfWeek.THURSDAY -> "Thursday"
            DayOfWeek.FRIDAY -> "Friday"
            DayOfWeek.SATURDAY -> "Saturday"
            DayOfWeek.SUNDAY -> "Sunday"
            else -> throw IllegalArgumentException()
        }

    /**
     * Get a string of the month provided
     */
    fun Month.toMonthShortenedString(): String =
        when (this) {
            Month.JANUARY -> "Jan"
            Month.FEBRUARY -> "Feb"
            Month.MARCH -> "Mar"
            Month.APRIL -> "Apr"
            Month.MAY -> "May"
            Month.JUNE -> "Jun"
            Month.JULY -> "Jul"
            Month.AUGUST -> "Aug"
            Month.SEPTEMBER -> "Sep"
            Month.OCTOBER -> "Oct"
            Month.NOVEMBER -> "Nov"
            Month.DECEMBER -> "Dec"
            else -> throw IllegalArgumentException()
        }

    /**
     * Get a full string of the month provided
     */
    fun Month.toMonthString(): String =
        when (this) {
            Month.JANUARY -> "January"
            Month.FEBRUARY -> "February"
            Month.MARCH -> "March"
            Month.APRIL -> "April"
            Month.MAY -> "May"
            Month.JUNE -> "June"
            Month.JULY -> "July"
            Month.AUGUST -> "August"
            Month.SEPTEMBER -> "September"
            Month.OCTOBER -> "October"
            Month.NOVEMBER -> "November"
            Month.DECEMBER -> "December"
            else -> throw IllegalArgumentException()
        }

    fun LocalDateTime.plus(
        years: Int = 0,
        months: Int = 0,
        days: Int = 0,
        hours: Int = 0,
        minutes: Int = 0,
        seconds: Int = 0,
        timeZoneId: String = TimeZone.currentSystemDefault().id,
    ): LocalDateTime {
        val timeZone = TimeZone.of(timeZoneId)
        val instant = this.toInstant(timeZone)
        val dateTimePeriod = DateTimePeriod(years, months, days, hours, minutes, seconds)
        return instant.plus(dateTimePeriod, timeZone)
            .toLocalDateTime(timeZone)
    }
}