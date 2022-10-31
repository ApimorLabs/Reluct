package work.racka.reluct.android.compose.components.number_picker

import kotlin.math.roundToInt
import kotlin.math.roundToLong

/**
 * Convert the provided time in milliseconds to Hours and Minutes Pair.
 * The first item is hours the second is minutes.
 */
fun convertMillisToTime(millis: Long): Hours {
    val hours = (millis / 3.6e6).toInt()
    val minutesMillis = millis - (hours * 3.6e6)
    val minutes = (minutesMillis / 60_000L).roundToInt()
    return FullHours(hours, minutes)
}

/**
 * Convert the provided hours and minutes to milliseconds.
 */
fun convertTimeToMillis(time: Hours): Long {
    val hoursMillis = time.hours * 3.6e6
    val minutesMillis = time.minutes * 60_000L
    return (hoursMillis + minutesMillis).roundToLong()
}
