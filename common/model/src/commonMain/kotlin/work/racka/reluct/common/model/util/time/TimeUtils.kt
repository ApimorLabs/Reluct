package work.racka.reluct.common.model.util.time

import kotlinx.datetime.*
import kotlin.math.abs

object TimeUtils {

    /**
     * Retrieve the correct LocalDateTime with account to TimeZone
     */
    private fun getLocalDateTimeWithCorrectTimeZone(
        dateTime: String,
        originalTimeZoneId: String
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
     * This accounts for changes in Timezone. So you need the TimeZoneId
     * The TimeZoneId needed is the one from the database. This will convert
     * the LocalDateTime stored to the correct system TimeZone if it's different
     * It will return a Time string in the format of HH:MM
     */
    fun getTimeFromLocalDateTime(
        dateTime: String,
        timeZoneId: String = TimeZone.currentSystemDefault().id
    ): String {
        val localDateTime = getLocalDateTimeWithCorrectTimeZone(dateTime, timeZoneId)
        val hr = localDateTime.hour
        val min =
            if (localDateTime.minute < 10) "0${localDateTime.minute}" else localDateTime.minute
        return "$hr:$min"
    }

    /**
     * This accounts for changes in Timezone. So you need the TimeZoneId
     * The TimeZoneId needed is the one from the database. This will convert
     * the LocalDateTime stored to the correct system TimeZone if it's different
     * It will return a countdown or time passed since the provided LocalDateTime
     */
    fun getTimeLeftFromLocalDateTime(
        dateTime: String,
        timeZoneId: String = TimeZone.currentSystemDefault().id
    ): String {
        val currentTime = Clock.System.now()
        val instant = getLocalDateTimeWithCorrectTimeZone(dateTime, timeZoneId)
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
}