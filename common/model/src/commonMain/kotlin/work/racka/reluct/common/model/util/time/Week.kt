package work.racka.reluct.common.model.util.time

import kotlinx.datetime.*

enum class Week(
    val isoDayNumber: Int,
    val dayAcronym: String,
) {
    MONDAY(
        isoDayNumber = DayOfWeek.MONDAY.isoDayNumber,
        dayAcronym = "Mon"
    ),
    TUESDAY(
        isoDayNumber = DayOfWeek.TUESDAY.isoDayNumber,
        dayAcronym = "Tue"
    ),
    WEDNESDAY(
        isoDayNumber = DayOfWeek.WEDNESDAY.isoDayNumber,
        dayAcronym = "Wed"
    ),
    THURSDAY(
        isoDayNumber = DayOfWeek.THURSDAY.isoDayNumber,
        dayAcronym = "Thu"
    ),
    FRIDAY(
        isoDayNumber = DayOfWeek.FRIDAY.isoDayNumber,
        dayAcronym = "Fri"
    ),
    SATURDAY(
        isoDayNumber = DayOfWeek.SATURDAY.isoDayNumber,
        dayAcronym = "Sat"
    ),
    SUNDAY(
        isoDayNumber = DayOfWeek.SUNDAY.isoDayNumber,
        dayAcronym = "Sun"
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