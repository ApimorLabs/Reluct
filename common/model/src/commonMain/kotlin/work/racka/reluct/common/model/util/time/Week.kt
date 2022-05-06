package work.racka.reluct.common.model.util.time

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber

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