package work.racka.reluct.compose.common.date.time.picker.date

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.datetime.LocalDate

internal class DatePickerState(
    initialDate: LocalDate,
    val colors: DatePickerColors,
    val yearRange: IntRange,
) {
    var selected by mutableStateOf(initialDate)
    var yearPickerShowing by mutableStateOf(false)

    companion object {
        val dayHeaders = listOf("S", "M", "T", "W", "T", "F", "S")
    }
}
