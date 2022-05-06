package work.racka.reluct.common.model.util.time

import kotlinx.datetime.*

enum class Week(
    val isoDayNumber: Int,
) {
    MONDAY(
        isoDayNumber = DayOfWeek.MONDAY.isoDayNumber
    ),
    TUESDAY(
        isoDayNumber = DayOfWeek.TUESDAY.isoDayNumber
    ),
    WEDNESDAY(
        isoDayNumber = DayOfWeek.WEDNESDAY.isoDayNumber
    ),
    THURSDAY(
        isoDayNumber = DayOfWeek.THURSDAY.isoDayNumber
    ),
    FRIDAY(
        isoDayNumber = DayOfWeek.FRIDAY.isoDayNumber
    ),
    SATURDAY(
        isoDayNumber = DayOfWeek.SATURDAY.isoDayNumber
    ),
    SUNDAY(
        isoDayNumber = DayOfWeek.SUNDAY.isoDayNumber
    );
}

object WeekUtils {
    fun currentDayOfWeek(): Week {
        val ktxDayOfWeek = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            .dayOfWeek
        return Week.values()
            .first { it.isoDayNumber == ktxDayOfWeek.isoDayNumber }
    }
}