package work.racka.reluct.data.local.usagestats

import java.util.*

enum class Week(
    val day: String,
    val value: Int
) {
    MONDAY("Monday", Calendar.MONDAY),
    TUESDAY("Tuesday", Calendar.TUESDAY),
    WEDNESDAY("Wednesday", Calendar.WEDNESDAY),
    THURSDAY("Thursday", Calendar.THURSDAY),
    FRIDAY("Friday", Calendar.FRIDAY),
    SATURDAY("Saturday", Calendar.SATURDAY),
    SUNDAY("Sunday", Calendar.SUNDAY);
}