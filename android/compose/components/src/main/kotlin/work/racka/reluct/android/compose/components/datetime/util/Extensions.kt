package work.racka.reluct.android.compose.components.datetime.util

import androidx.compose.ui.geometry.Offset
import kotlinx.datetime.*
import work.racka.reluct.common.model.util.time.TimeUtils.toDayOfWeekShortenedString
import work.racka.reluct.common.model.util.time.TimeUtils.toMonthShortenedString
import kotlin.math.cos
import kotlin.math.sin

data class YearMonth(
    val year: Int,
    val month: Month,
) {
    val monthValue: Int = month.number
}

internal fun Float.getOffset(angle: Double): Offset =
    Offset((this * cos(angle)).toFloat(), (this * sin(angle)).toFloat())

internal val LocalDate.yearMonth: YearMonth
    get() = YearMonth(this.year, this.month)

internal val Month.monthString: String
    get() = this.toMonthShortenedString()

internal val DayOfWeek.shortLocalName: String
    get() = this.toDayOfWeekShortenedString()

internal val LocalDateTime.isAM: Boolean
    get() = this.hour in 0..11

internal val LocalDateTime.simpleHour: Int
    get() {
        val tempHour = this.hour % 12
        return if (tempHour == 0) 12 else tempHour
    }

internal fun LocalDateTime.toAM(): LocalDateTime =
    if (this.isAM) {
        this
    } else {
        LocalDateTime(
            this.year,
            this.monthNumber,
            this.dayOfMonth,
            this.hour - 12,
            this.minute,
            this.second,
            this.nanosecond
        )
    }

internal fun LocalDateTime.toPM(): LocalDateTime =
    if (!this.isAM) {
        this
    } else {
        LocalDateTime(
            this.year,
            this.monthNumber,
            this.dayOfMonth,
            this.hour + 12,
            this.minute,
            this.second,
            this.nanosecond
        )
    }

internal fun currentLocalDateTime(): LocalDateTime =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

internal fun currentLocalDate(): LocalDate {
    val localDateTime = currentLocalDateTime()
    return LocalDate(localDateTime.year, localDateTime.monthNumber, localDateTime.dayOfMonth)
}

internal fun LocalDateTime.withHour(hour: Int): LocalDateTime =
    LocalDateTime(
        this.year,
        this.monthNumber,
        this.dayOfMonth,
        hour,
        this.minute,
        this.second,
        this.nanosecond
    )

internal fun LocalDateTime.withMinute(minute: Int): LocalDateTime =
    LocalDateTime(
        this.year,
        this.monthNumber,
        this.dayOfMonth,
        this.hour,
        minute,
        this.second,
        this.nanosecond
    )

internal object LocalDateTimeRange {
    val MIN
        get() = LocalDateTime(-999_999_999, 1, 1, 0, 0, 0, 0)
    val MAX
        get() = LocalDateTime(999_999_999, 12, 31, 23, 59, 59, 999_999_999)
}
