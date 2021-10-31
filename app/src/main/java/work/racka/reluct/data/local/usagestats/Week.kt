package work.racka.reluct.data.local.usagestats

import java.util.*

enum class Week(
    val day: String,
    val value: Int
) {
    SUNDAY("Sun", Calendar.SUNDAY),
    MONDAY("Mon", Calendar.MONDAY),
    TUESDAY("Tue", Calendar.TUESDAY),
    WEDNESDAY("Wed", Calendar.WEDNESDAY),
    THURSDAY("Thu", Calendar.THURSDAY),
    FRIDAY("Fri", Calendar.FRIDAY),
    SATURDAY("Sat", Calendar.SATURDAY);
}